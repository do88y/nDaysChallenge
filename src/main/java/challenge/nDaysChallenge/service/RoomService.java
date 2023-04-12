package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.RoomMember;
import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.StampDto;
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

import java.util.*;

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
    public List<SingleRoom> findSingleRooms(Member member) {
        return singleRoomRepository.findSingleRooms(member);
    }
    public List<GroupRoom> findGroupRooms(Member member) {
        return groupRoomRepository.findGroupRooms(member);
    }

    /**
     * 개인 챌린지 생성
     */
    @Transactional
    public RoomResponseDto singleRoom(Member member, String name, Period period, Category category, int passCount, String reward) {

        //챌린지 생성
        SingleRoom newRoom = new SingleRoom(name, new Period(period.getStartDate(), period.getTotalDays()), category, passCount, reward);

        //스탬프 생성
        Stamp stamp = Stamp.createStamp(newRoom, member);

        //저장
        singleRoomRepository.save(newRoom);
        stampRepository.save(stamp);

        //멤버에 챌린지 저장
        newRoom.addRoom(newRoom, member, stamp);

        RoomResponseDto roomDto = createRoomDto(newRoom, stamp);

        return roomDto;
    }

    /**
     * 그룹 챌린지 생성
     */
    @Transactional
    public GroupRoom groupRoom(Member member, String name, Period period, Category category, int passCount, String reward, int usedPassCount, int successCount, Set<Long> selectedMember) {

        //엔티티 조회
        Set<Member> memberList = new HashSet<>();
        for (Long memberNumber : selectedMember) {
            Member findMember = memberRepository.findByNumber(memberNumber).orElseThrow(
                    () -> new RuntimeException("해당 멤버가 존재하지 않습니다."));
            memberList.add(findMember);
        }

        //챌린지 생성
        GroupRoom newRoom = new GroupRoom(member, name, new Period(period.getStartDate(), period.getTotalDays()), category, passCount, reward);
        groupRoomRepository.save(newRoom);

        //방장
        //스탬프 생성
        Stamp stamp = Stamp.createStamp(newRoom, member);
        stampRepository.save(stamp);
        //룸멤버 생성
        RoomMember roomMember = RoomMember.createRoomMember(member, newRoom, stamp);
        roomMemberRepository.save(roomMember);

        //그 외 멤버
        for (Member findMember : memberList) {
            //스탬프 생성
            Stamp newStamp = Stamp.createStamp(newRoom, findMember);
            stampRepository.save(newStamp);
            //룸멤버 생성
            RoomMember result = RoomMember.createRoomMember(findMember, newRoom, newStamp);
            roomMemberRepository.save(result);
        }

        return newRoom;
    }

    /**
     * 스탬프 찍기
     */
    @Transactional
    public StampDto updateStamp(Member member, Long roomNumber, StampDto dto) {

        //엔티티 조회
        Stamp stamp = stampRepository.findById(dto.getStampNumber()).orElseThrow(
                () -> new NoSuchElementException("해당 스탬프가 존재하지 않습니다."));
        Room room = roomRepository.findByNumber(roomNumber).orElseThrow(
                () -> new NoSuchElementException("해당 챌린지가 존재하지 않습니다."));

        //스탬프 엔티티 업데이트
        Stamp updateStamp = stamp.updateStamp(room, dto.getDay());

        //count 업데이트
        if (stamp.getLatestStamp().equals("o")) {
            stamp.addSuccess();
        } else if (stamp.getLatestStamp().equals("x")) {
            stamp.addPass();
        } else {
            throw new RuntimeException("스탬프 정보를 얻을 수 없습니다.");
        }

        //dto 생성
        StampDto stampDto = getStampDto(roomNumber, stamp, updateStamp);

        return stampDto;
    }

    /**
     * 챌린지 삭제
     */
    @Transactional
    public void deleteRoom(Member member, Long roomNumber) {

        //엔티티 조회
        Room room = roomRepository.findByNumber(roomNumber).orElseThrow(
                () -> new NoSuchElementException("해당 챌린지가 존재하지 않습니다."));

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
    public List<SingleRoom> findFinishedSingleRooms(Member member) {
        try {
            return singleRoomRepository.finishedSingleRooms(member);
        } catch (Exception e) {
            throw new RuntimeException("완료된 개인 챌린지가 없습니다.");
        }
    }
    public List<GroupRoom> findFinishedGroupRooms(Member member) {
        try {
            return groupRoomRepository.finishedGroupRoom(member);
        } catch (Exception e) {
            throw new RuntimeException("완료된 단체 챌린지가 없습니다.");
        }
    }

    //==공통 메서드==//
    //roomDto 생성
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

    //stampDto 생성
    private static StampDto getStampDto(Long roomNumber, Stamp stamp, Stamp updateStamp) {
        StampDto stampDto = StampDto.builder()
                .roomNumber(roomNumber)
                .stampNumber(updateStamp.getNumber())
                .day(updateStamp.getDay())
                .successCount(stamp.getSuccessCount())
                .usedPassCount(stamp.getUsedPassCount())
                .build();
        return stampDto;
    }

}
