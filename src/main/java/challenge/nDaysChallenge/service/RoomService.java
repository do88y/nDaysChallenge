package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.*;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.RoomRequestDTO;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.GroupRoomRepository;
import challenge.nDaysChallenge.repository.room.SingleRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final GroupRoomRepository groupRoomRepository;
    private final SingleRoomRepository singleRoomRepository;


    //개인과 그룹 챌린지 구분
    public Room createRoom(Member member, RoomRequestDTO dto) {

        Optional<String> getRoomType = Optional.ofNullable(dto.getType());
        String roomType = getRoomType.orElseThrow(() -> new NoSuchElementException("챌린지 타입이 없습니다."));

        if (roomType.equals("SINGLE")) {
            SingleRoom singleRoom = singleRoom(member, dto.getName(), new Period(dto.getStartDate(), dto.getTotalDays()), Category.valueOf(dto.getCategory()), dto.getPassCount(), dto.getReward());

            return singleRoom;
        } else if (roomType.equals("GROUP")) {
            Set<Long> groupMemberNums = dto.getGroupMembers();
            Set<Member> groupMembers = new HashSet<>();  //멤버 타입의 새로운 HashSet
            for (Long groupMemberNum : groupMemberNums) {
                groupMembers.add(memberRepository.findByNumber(groupMemberNum));
            }
            GroupRoom groupRoom = groupRoom(member, dto.getName(), new Period(dto.getStartDate(), dto.getTotalDays()), Category.valueOf(dto.getCategory()), dto.getPassCount(), dto.getReward(), groupMembers);

            return groupRoom;
        }
        return null;
    }

    /**
     * 개인 챌린지 생성
     */
    @Transactional
    public SingleRoom singleRoom(Member member, String name, Period period, Category category, int passCount, String reward) {

        //챌린지 생성
        SingleRoom newRoom = new SingleRoom(name, new Period(period.getStartDate(), period.getTotalDays()), category, passCount, reward);

        //챌린지 저장
        singleRoomRepository.save(newRoom);

        //멤버에 챌린지 저장
        newRoom.addRoom(newRoom, member);

        return newRoom;
    }

    /**
     * 그룹 챌린지 생성
     */
    @Transactional
    public GroupRoom groupRoom(Member member, String name, Period period, Category category, int passCount, String reward, Set<Member> selectedMember) {

        //챌린지 생성
        GroupRoom newRoom = new GroupRoom(name, new Period(period.getStartDate(), period.getTotalDays()), category, passCount, reward);


        //챌린지 저장
        groupRoomRepository.save(newRoom);

        //챌린지 멤버 생성&저장
        RoomMember roomMember = RoomMember.createRoomMember(member, newRoom);  //방장
        roomMemberRepository.save(roomMember);
        for (Member members : selectedMember) {  //그 외 멤버
            RoomMember result = RoomMember.createRoomMember(members, newRoom);
            roomMemberRepository.save(result);
        }

        return newRoom;
    }

    /**
     * 챌린지 삭제
     */
    @Transactional
    public void deleteRoom(Member member, Long roomNumber) {
        //엔티티 조회
        SingleRoom room = singleRoomRepository.findById(roomNumber).get();
        Set<RoomMember> roomMembers = roomMemberRepository.findByRoomNumber(roomNumber);

        if (room.getType() == RoomType.GROUP) {
            //단체 챌린지 삭제
            roomRepository.delete(room);

            //roomCount -1, RoomMember 삭제
            for (RoomMember roomMember : roomMembers) {
                roomMemberRepository.delete(roomMember);  //Member의 roomMemberList에서도 삭제 됨
            }

        } else if (room.getType() == RoomType.SINGLE) {
            //개인 챌린지 삭제
            singleRoomRepository.delete(room);
            member.getSingleRooms().remove(room);
        }

    }

    /**
     * 개인 챌린지 실패
     */
    @Transactional
    public void failSingleRoom(Long roomNumber) {
        //엔티티 조회
        SingleRoom singleRoom = singleRoomRepository.findById(roomNumber).get();

        //개인 챌린지 멤버 조회
        Member member = SingleRoom.giveMember();
        int usedPassCount = singleRoom.getUsedPassCount();
        int passCount = singleRoom.getPassCount();

        if (usedPassCount > passCount) {
            roomRepository.deleteById(member.getNumber());
        }
    }

    /**
     * 단체 챌린지 실패
     */
    @Transactional
    public void failGroupRoom(Long roomNumber) {
        //엔티티 조회
        Optional<GroupRoom> groupRoom = groupRoomRepository.findById(roomNumber);

        //그룹 챌린지 멤버 조회
        List<RoomMember> roomMembers = groupRoom.get().getRoomMemberList();
        int usedPassCount = groupRoom.get().getUsedPassCount();
        int passCount = groupRoom.get().getPassCount();

        if (usedPassCount > passCount) {
            for (RoomMember roomMember : roomMembers) {
                roomRepository.deleteById(roomMember.getNumber());
            }
        }
    }

    /**
     * 전체 완료 챌린지 조회
     */
    public List<Room> findFinishedRooms(Member member) {

        List<Room> finishedRoom = new ArrayList<>();

        if (member.getRoomMemberList() != null) {
            List<RoomMember> roomMemberList = member.getRoomMemberList();
            for (RoomMember roomMember : roomMemberList) {
                Room groupRoom = roomMember.getRoom();
                if (groupRoom.getStatus() == RoomStatus.END) {
                    finishedRoom.add(groupRoom);
                }
            }
        }
        if (member.getSingleRooms() != null) {
            List<SingleRoom> singleRooms = member.getSingleRooms();
            for (Room singleRoom : singleRooms) {
                if (singleRoom.getStatus() == RoomStatus.END) {
                    finishedRoom.add(singleRoom);
                }
            }
        }

        return finishedRoom;
    }

}
