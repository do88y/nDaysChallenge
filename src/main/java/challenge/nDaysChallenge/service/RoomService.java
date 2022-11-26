package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.*;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.GroupRoomRepository;
import challenge.nDaysChallenge.repository.room.SingleRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor  //final 붙은 필드로만 생성자를 만들어 줌
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;
    private final GroupRoomRepository groupRoomRepository;
    private final SingleRoomRepository singleRoomRepository;


    //단체 챌린지 조회
    public List<GroupRoom> findAllGroupRoomUsingFetchJoin() {
        return groupRoomRepository.findAllWithRoomMemberUsingFetchJoin();
    }

    //특정 챌린지 조회
    public Optional<Room> findById(Long number) {
        return roomRepository.findById(number);
    }

    /**
     * 그룹 챌린지 생성
     */
    @Transactional
    public Long groupRoom(Long memberNumber, String name, Period period, Category category, int passCount, Member... selectedMember) {

        //엔티티 조회
        Member member = memberRepository.findByNumber(memberNumber);

        //챌린지 생성
        Room newRoom = GroupRoom.builder()
                .name(name)
                .period(new Period(period.getTotalDays()))
                .category(category)
                .type(RoomType.GROUP)
                .passCount(passCount)
                .build();

        //챌린지 저장
        roomRepository.save(newRoom);

        //챌린지 멤버 생성
        RoomMember roomMember = RoomMember.createRoomMember(member, newRoom);  //방장
        roomMemberRepository.save(roomMember);
        for (Member members : selectedMember) {  //그 외 멤버
            RoomMember result = RoomMember.createRoomMember(members, newRoom);
            roomMemberRepository.save(result);
        }

        //챌린지 멤버 저장

        return newRoom.getNumber();
    }

    /**
     * 개인 챌린지 생성
     */
    @Transactional
    public Long singleRoom(Long memberNumber, String name, Period period, Category category, int passCount) {

        //엔티티 조회
        Member member = memberRepository.findByNumber(memberNumber);

        //챌린지 생성
        Room newRoom = SingleRoom.builder()
                .name(name)
                .period(new Period(period.getTotalDays()))
                .category(category)
                .type(RoomType.SINGLE)
                .passCount(passCount)
                .build();

        //챌린지 저장
        roomRepository.save(newRoom);

        //SingleRoom에 Member 입력
        SingleRoom.addMember(member);

        //챌린지 멤버 생성
        SingleRoom addRoomMember = SingleRoom.addRoom(newRoom);

        //챌린지 멤버 저장
        roomRepository.save(addRoomMember);

        return newRoom.getNumber();
    }


    /**
     * 챌린지 삭제
     */
    @Transactional
    public void deleteRoom(Long memberNumber, Long roomNumber) {
        //엔티티 조회
        SingleRoom room = singleRoomRepository.findById(roomNumber).get();
        List<RoomMember> roomMembers = roomMemberRepository.findByRoomNumber(roomNumber);
        Member member = memberRepository.findByNumber(memberNumber);

        if (room.getType() == RoomType.GROUP) {
            //단체 챌린지 삭제
            roomRepository.delete(room);

            //roomCount -1, RoomMember 삭제
            for (RoomMember roomMember : roomMembers) {
                roomMember.reduceCount();
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
        GroupRoom groupRoom = groupRoomRepository.findById(roomNumber).get();

        //그룹 챌린지 멤버 조회
        List<RoomMember> roomMembers = groupRoom.getRoomMemberList();
        int usedPassCount = groupRoom.getUsedPassCount();
        int passCount = groupRoom.getPassCount();

        if (usedPassCount > passCount) {
            for (RoomMember roomMember : roomMembers) {
                roomRepository.deleteById(roomMember.getNumber());

            }
        }

        //roomCount +1
        RoomMember roomMember = roomMemberRepository.findByMemberNumber(roomNumber);
        roomMember.addCount();

    }

    /**
     * 챌린지 갯수 검색
     */
    public int findRoomCount(Long memberNumber) {
        RoomMember roomMember = roomMemberRepository.findByMemberNumber(memberNumber);
        int roomCount = roomMember.getRoomCount();
        return roomCount;
    }

}
