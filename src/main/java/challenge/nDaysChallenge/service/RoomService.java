package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.RoomMember;
import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.StampDto;
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

        //스탬프 생성
        Stamp stamp = Stamp.createStamp(newRoom);

        //저장
        singleRoomRepository.save(newRoom);
        stampRepository.save(stamp);

        //멤버에 챌린지 저장
        newRoom.addRoom(newRoom, member, stamp);

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
            Member findMember = memberRepository.findByNumber(memberNumber).orElseThrow(
                    () -> new RuntimeException("해당 멤버가 존재하지 않습니다."));
            memberList.add(findMember);
        }

        //챌린지 생성
        GroupRoom newRoom = new GroupRoom(member, name, new Period(period.getStartDate(), period.getTotalDays()), category, passCount, reward, usedPassCount, successCount);
        groupRoomRepository.save(newRoom);

        //방장
        //스탬프 생성
        Stamp stamp = Stamp.createStamp(newRoom);
        stampRepository.save(stamp);
        //룸멤버 생성
        RoomMember roomMember = RoomMember.createRoomMember(member, newRoom, stamp);
        roomMemberRepository.save(roomMember);

        Map<String, Long> stamps = newRoom.getStamps();
        stamps.put(member.getNickname(), stamp.getNumber());

        //그 외 멤버
        for (Member fidnMember : memberList) {
            Member member1 = memberRepository.findByNumber(fidnMember.getNumber()).get();
            //스탬프 생성
            Stamp newStamp = Stamp.createStamp(newRoom);
            stampRepository.save(newStamp);
            //룸멤버 생성
            RoomMember result = RoomMember.createRoomMember(fidnMember, newRoom, newStamp);
            roomMemberRepository.save(result);

            RoomMember findRoomMember = roomMemberRepository.findByMemberAndRoom(member1, newRoom);
            stamps.put(findRoomMember.getMember().getNickname(), newStamp.getNumber());
        }

        return newRoom;
    }

    /**
     * 스탬프 찍기
     */
    @Transactional
    public Stamp updateStamp(Member member, Long roomNumber, StampDto dto) {

        //엔티티 조회
        Stamp stamp = stampRepository.findById(dto.getStampNumber()).orElseThrow(
                () -> new NoSuchElementException("해당 스탬프가 존재하지 않습니다."));
        Room room = roomRepository.findByNumber(roomNumber).orElseThrow(
                () -> new NoSuchElementException("해당 챌린지가 존재하지 않습니다."));

        //스탬프 엔티티 업데이트
        Stamp updateStamp = stamp.updateStamp(room, dto.getDay());

        stampRepository.save(updateStamp);

        return updateStamp;
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
            for (RoomMember roomMember : roomMembers) {
                roomMemberRepository.delete(roomMember);  //Member의 roomMemberList에서도 삭제 됨
            }
            //그룹 챌린지 삭제
            roomRepository.delete(room);
        }
    }

    /**
     * 개인 챌린지 실패
     */
    @Transactional
    public void failSingleRoom(Long roomNumber) {
        //엔티티 조회
        SingleRoom singleRoom = singleRoomRepository.findById(roomNumber).orElseThrow(
                () -> new NoSuchElementException("해당 개인 챌린지가 존재하지 않습니다"));

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
        GroupRoom groupRoom = groupRoomRepository.findById(roomNumber).orElseThrow(
                () -> new NoSuchElementException("해당 개인 챌린지가 존재하지 않습니다"));

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

    //작성자가 맞는지 확인
/*    private void userCheck(Member currentMember, Member writeMember) {
        if (!currentMember.getNumber().equals(writeMember)) {
            throw new RuntimeException("접근 권한이 없습니다.");
        }
    }*/

}
