package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
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


    /**
     * 챌린지 조회(메인)
     */
    public List<SingleRoom> findSingleRooms(Member member) {
        try {
            return singleRoomRepository.findSingleRooms(member);
        } catch (Exception e) {
            throw new RuntimeException("진행중인 개인 챌린지가 없습니다.");
        }
    }
    public List<GroupRoom> findGroupRooms(Member member) {
        try {
            return groupRoomRepository.findGroupRooms(member);
        } catch (Exception e) {
            throw new RuntimeException("진행중인 단체 챌린지가 없습니다.");
        }
    }

    /**
     * 개인 챌린지 생성
     */
    @Transactional
    public SingleRoom singleRoom(Member member, String name, Period period, Category category, int passCount, String reward, int usedPassCount, int successCount) {

        //챌린지 생성
        SingleRoom newRoom = new SingleRoom(name, new Period(period.getStartDate(), period.getTotalDays()), category, passCount, reward, usedPassCount, successCount);

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
    public GroupRoom groupRoom(Member member, String name, Period period, Category category, int passCount, String reward, int usedPassCount, int successCount, Set<Long> selectedMember) {

        //엔티티 조회
        Set<Member> memberList = new HashSet<>();
        for (Long memberNumber : selectedMember) {
            Optional<Member> findMember = memberRepository.findByNumber(memberNumber);
            memberList.add(findMember.orElseThrow(() -> new RuntimeException("해당 멤버가 존재하지 않습니다.")));
        }

        //챌린지 생성
        GroupRoom newRoom = new GroupRoom(name, new Period(period.getStartDate(), period.getTotalDays()), category, passCount, reward, usedPassCount, successCount);

        //챌린지 저장
        groupRoomRepository.save(newRoom);

        //챌린지 멤버 생성&저장
        RoomMember roomMember = RoomMember.createRoomMember(member, newRoom);  //방장
        roomMemberRepository.save(roomMember);
        for (Member members : memberList) {  //그 외 멤버
            RoomMember result = RoomMember.createRoomMember(members, newRoom);
            roomMemberRepository.save(result);
        }

        return newRoom;
    }

    /**
     * 챌린지 삭제
     */
    @Transactional
    public void deleteRoom(Long roomNumber) {

        //엔티티 조회
        Optional<Room> findRoom = roomRepository.findById(roomNumber);
        Room room = findRoom.orElseThrow(() -> new NoSuchElementException("해당 챌린지가 존재하지 않습니다."));

        if (room.getType() == RoomType.SINGLE) {
            //개인 챌린지 삭제
            roomRepository.delete(room);

        } else if (room.getType() == RoomType.GROUP) {
            //RoomMember 삭제 - room 같이 삭제 됨
            Set<RoomMember> roomMembers = roomMemberRepository.findByRoom(room);
            for (RoomMember roomMember : roomMembers) {
                roomMemberRepository.delete(roomMember);  //Member의 roomMemberList에서도 삭제 됨
            }
        }
    }

    /**
     * 개인 챌린지 실패
     */
    @Transactional
    public void failSingleRoom(Long roomNumber) {
        //엔티티 조회
        Optional<SingleRoom> findRoom = singleRoomRepository.findById(roomNumber);
        SingleRoom singleRoom = findRoom.orElseThrow(() -> new NoSuchElementException("해당 개인 챌린지가 존재하지 않습니다"));

        //개인 챌린지 멤버 조회
        Member member = singleRoom.giveMember();
        int usedPassCount = singleRoom.getUsedPassCount();
        int passCount = singleRoom.getPassCount();

        if (usedPassCount > passCount) {
            roomRepository.deleteById(roomNumber);
        }
    }

    /**
     * 단체 챌린지 실패
     */
    @Transactional
    public void failGroupRoom(Long roomNumber) {
        //엔티티 조회
        Optional<GroupRoom> findRoom = groupRoomRepository.findById(roomNumber);
        GroupRoom groupRoom = findRoom.orElseThrow(() -> new NoSuchElementException("해당 개인 챌린지가 존재하지 않습니다"));

        //그룹 챌린지 멤버 조회
        List<RoomMember> roomMembers = groupRoom.getRoomMemberList();
        int usedPassCount = groupRoom.getUsedPassCount();
        int passCount = groupRoom.getPassCount();

        if (usedPassCount > passCount) {
            for (RoomMember roomMember : roomMembers) {
                roomRepository.deleteById(roomMember.getNumber());
            }
        }
    }

    /**
     * 상태 완료로 변경
     */
    @Transactional
    public void changeStatus(Long roomNumber) {
        Optional<Room> findRoom = roomRepository.findById(roomNumber);
        Room room = findRoom.orElseThrow(() -> new NoSuchElementException("해당 챌린지가 존재하지 않습니다."));

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

}
