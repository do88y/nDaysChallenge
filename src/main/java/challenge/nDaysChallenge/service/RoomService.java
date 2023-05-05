package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.RoomMember;
import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.Room.GroupRoomRequestDto;
import challenge.nDaysChallenge.dto.request.StampDto;
import challenge.nDaysChallenge.dto.response.room.GroupRoomResponseDto;
import challenge.nDaysChallenge.dto.response.room.RoomResponseDto;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.StampRepository;
import challenge.nDaysChallenge.repository.room.GroupRoomRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.SingleRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final GroupRoomRepository groupRoomRepository;
    private final SingleRoomRepository singleRoomRepository;
    private final StampRepository stampRepository;


    /**
     * 챌린지 조회(메인)
     */
    public List<RoomResponseDto> findRooms(String id) {
        List<SingleRoom> singleRooms = singleRoomRepository.findSingleRooms(id);
        List<GroupRoom> groupRooms = groupRoomRepository.findGroupRooms(id);
        List<Room> rooms = Stream.concat(singleRooms.stream(), groupRooms.stream()).collect(Collectors.toList());
        return roomDtos(rooms);
    }

    /**
     * 개인 챌린지 생성
     */
    @Transactional
    public RoomResponseDto singleRoom(String id, String name, Period period, Category category, int passCount, String reward) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 id의 사용자를 찾아오는 데 실패했습니다."));

        //챌린지 생성
        SingleRoom newRoom = new SingleRoom(name, new Period(period.getStartDate(), period.getTotalDays()), category, passCount, reward);
        //스탬프 생성
        Stamp stamp = Stamp.createStamp(newRoom);
        //챌린지에 멤버, 스탬프 추가
        newRoom.addRoom(member, stamp);

        //저장
        stampRepository.save(stamp);
        singleRoomRepository.save(newRoom);

        return createRoomDto(newRoom, stamp);
    }

    /**
     * 그룹 챌린지 생성
     */
    @Transactional
    public GroupRoomResponseDto groupRoom(String id, String name, Period period, Category category, int passCount, String reward, Set<Long> selectedMember) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 id의 사용자를 찾아오는 데 실패했습니다."));

        //엔티티 조회
        Set<Member> memberList = new HashSet<>();
        for (Long memberNumber : selectedMember) {
            Member findMember = memberRepository.findByNumber(memberNumber).orElseThrow(
                    () -> new RuntimeException("해당 멤버가 존재하지 않습니다."));
            memberList.add(findMember);
        }

        //챌린지 생성
        GroupRoom newRoom = new GroupRoom(member, name, new Period(period.getStartDate(), period.getTotalDays()), category, passCount, reward);

        //방장
        //스탬프 생성
        Stamp stamp = Stamp.createStamp(newRoom);
        //룸멤버 생성
        RoomMember roomMember = RoomMember.createRoomMember(member, newRoom, stamp);

        //저장
        groupRoomRepository.save(newRoom);
        stampRepository.save(stamp);
        roomMemberRepository.save(roomMember);

        //그 외 멤버
        for (Member findMember : memberList) {
            //스탬프 생성
            Stamp newStamp = Stamp.createStamp(newRoom);
            //룸멤버 생성
            RoomMember result = RoomMember.createRoomMember(findMember, newRoom, newStamp);

            //저장
            stampRepository.save(newStamp);
            roomMemberRepository.save(result);
        }

        return createGroupRoomDto(newRoom, selectedMember);
    }

    /**
     * 스탬프 찍기
     */
    @Transactional
    public StampDto updateStamp(String id, Long roomNumber, StampDto dto) {

        //엔티티 조회
        Stamp stamp = stampRepository.findById(dto.getStampNumber()).orElseThrow(
                () -> new NoSuchElementException("해당 스탬프가 존재하지 않습니다."));
        Room room = roomRepository.findByNumber(roomNumber).orElseThrow(
                () -> new NoSuchElementException("해당 챌린지가 존재하지 않습니다."));
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("해당 멤버가 존재하지 않습니다."));
        checkStampAndMember(member, room, stamp);

        //스탬프 엔티티 업데이트
        Stamp updateStamp = stamp.updateStamp(room, dto.getDay());

        //count 업데이트
        if (StringUtils.hasText(dto.getDay())) {
            updateStamp.updateCount(dto.getDay());
        }

        return getStampDto(roomNumber, updateStamp);
    }

    /**
     * 챌린지 삭제
     */
    @Transactional
    public void deleteRoom(String id, Long roomNumber) {

        //엔티티 조회
        Room room = roomRepository.findByNumber(roomNumber).orElseThrow(
                () -> new NoSuchElementException("해당 챌린지가 존재하지 않습니다."));
        Member member = memberRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("해당 멤버가 존재하지 않습니다."));
        checkRoomAndMember(member, room);

        if (room.getType() == RoomType.SINGLE) {
            //개인 챌린지 삭제
            roomRepository.delete(room);

        } else if (room.getType() == RoomType.GROUP) {
            //RoomMember 삭제
            Set<RoomMember> roomMembers = roomMemberRepository.findByRoom(room);
            List<Stamp> findStamps = stampRepository.findByGroupRoom(room);
            roomMembers.forEach(roomMemberRepository::delete);//Member의 roomMemberList에서도 삭제 됨
            findStamps.forEach(stampRepository::delete);
            //그룹 챌린지 삭제
            roomRepository.delete(room);
        }
    }

    /**
     * 상태 완료로 변경
     */
    @Transactional
    public void changeStatus(Long roomNumber) {
        Room room = roomRepository.findByNumber(roomNumber).orElseThrow(
                () -> new NoSuchElementException("해당 챌린지가 존재하지 않습니다."));

        room.end();
    }

    /**
     * 전체 완료 챌린지 조회
     */
    public List<RoomResponseDto> findFinishedRooms(String id) {
        List<SingleRoom> singleRooms = singleRoomRepository.finishedSingleRooms(id);
        List<GroupRoom> groupRooms = groupRoomRepository.finishedGroupRoom(id);
        List<Room> rooms = Stream.concat(singleRooms.stream(), groupRooms.stream()).collect(Collectors.toList());
        return roomDtos(rooms);
    }


    //==공통 메서드==//
    //
    private static List<RoomResponseDto> roomDtos(List<Room> rooms) {
        List<RoomResponseDto> roomList = new ArrayList<>();
        rooms.forEach(room -> roomList.add(
                        RoomResponseDto.builder()
                                .roomNumber(room.getNumber())
                                .type(room.getType().name())
                                .name(room.getName())
                                .category(room.getCategory().name())
                                .totalDays(room.getPeriod().getTotalDays())
                                .startDate(room.getPeriod().getStartDate())
                                .endDate(room.getPeriod().getEndDate())
                                .passCount(room.getPassCount())
                                .reward(room.getReward())
                                .status(room.getStatus().name())
                                .build()));
        return roomList;
    }

    //챌린지 조회 응답 dto
    private RoomResponseDto createRoomDto(Room room) {
        RoomResponseDto roomResponseDto = RoomResponseDto.builder()
                .roomNumber(room.getNumber())
                .type(room.getType().name())
                .name(room.getName())
                .category(room.getCategory().name())
                .totalDays(room.getPeriod().getTotalDays())
                .startDate(room.getPeriod().getStartDate())
                .endDate(room.getPeriod().getEndDate())
                .passCount(room.getPassCount())
                .reward(room.getReward())
                .status(room.getStatus().name())
                .build();
        return roomResponseDto;
    }

    //개인챌린지 생성 응답 dto
    private RoomResponseDto createRoomDto(Room room, Stamp stamp) {
        RoomResponseDto roomResponseDto = RoomResponseDto.builder()
                .roomNumber(room.getNumber())
                .type(room.getType().name())
                .name(room.getName())
                .category(room.getCategory().name())
                .totalDays(room.getPeriod().getTotalDays())
                .startDate(room.getPeriod().getStartDate())
                .endDate(room.getPeriod().getEndDate())
                .passCount(room.getPassCount())
                .reward(room.getReward())
                .status(room.getStatus().name())
                .stamp(stamp.getNumber())
                .build();
        return roomResponseDto;
    }

    //그룹챌린지 생성 응답 dto
    private static GroupRoomResponseDto createGroupRoomDto(GroupRoom room, Set<Long> members) {
        GroupRoomResponseDto savedRoom = GroupRoomResponseDto.builder()
                .roomNumber(room.getNumber())
                .type(room.getType().name())
                .name(room.getName())
                .category(room.getCategory().name())
                .totalDays(room.getPeriod().getTotalDays())
                .startDate(room.getPeriod().getStartDate())
                .endDate(room.getPeriod().getEndDate())
                .passCount(room.getPassCount())
                .reward(room.getReward())
                .status(room.getStatus().name())
                .groupMembers(members)
                .build();
        return savedRoom;
    }

    //stampDto 생성
    private static StampDto getStampDto(Long roomNumber, Stamp updateStamp) {
        StampDto stampDto = StampDto.builder()
                .roomNumber(roomNumber)
                .stampNumber(updateStamp.getNumber())
                .day(updateStamp.getDay())
                .successCount(updateStamp.getSuccessCount())
                .usedPassCount(updateStamp.getUsedPassCount())
                .build();
        return stampDto;
    }

    //챌린지 삭제 시 인가 확인(방장만 삭제 가능)
    private void checkRoomAndMember(Member member, Room room) {
        Member findMember = roomRepository.findMemberByRoomNumber(room.getNumber()).orElseThrow(
                () -> new NoSuchElementException("삭제 권한이 없습니다."));
        if (findMember != member) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        }
    }

    //스탬프 업데이트 시 인가 확인
    private void checkStampAndMember(Member member, Room room, Stamp stamp) {
        if (RoomType.SINGLE == room.getType()) {
            Member findMember = roomRepository.findMemberByRoomNumber(room.getNumber()).orElseThrow(
                    () -> new NoSuchElementException("해당 챌린지에 member 데이터가 없습니다."));
            if (findMember != member) {
                throw new RuntimeException("스탬프에 접근 권한이 없습니다.");
            }

        } else if (RoomType.GROUP == room.getType()) {
            Member findMember = roomMemberRepository.findMemberByStamp(stamp);
            if (findMember != member) {
                throw new RuntimeException("스탬프에 접근 권한이 없습니다.");
            }
        }
    }
}
