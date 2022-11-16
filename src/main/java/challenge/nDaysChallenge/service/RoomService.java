package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.*;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RelationshipRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor  //final 붙은 필드로만 생성자를 만들어 줌
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final RoomMemberRepository roomMemberRepository;


    @Transactional
    public void saveRoom(Room room) {
        roomRepository.save(room);
    }

    /**
     * 챌린지 생성  **방장에게만 reduce()
     */
    @Transactional
    Long createRoom(Long memberNumber, String name, LocalDateTime startDate) {

        //엔티티 조회
        Room room = roomRepository.findById(memberNumber).get();
        Member member = memberRepository.findById(memberNumber).get();
        RoomMember roomMember = roomMemberRepository.findByMemberNumber(memberNumber);

        //챌린지 멤버 생성
        RoomMember setRoomMember = RoomMember.builder()
                .member(member)
                .room(room)
                .build();

        //챌린지 멤버 저장
        roomMemberRepository.save(roomMember);

        //챌린지 생성
        Room newRoom = Room.builder()
                .name(name).build();

        //roomCount -1
        roomMember.add();

        //챌린지 저장  **먼저 친구추가 필요
        roomRepository.save(newRoom);
        return newRoom.getNumber();
    }

    //챌린지 전체 조회
    public List<Room> findRooms() {
        return roomRepository.findAll();
    }

    //특정 챌린지 조회
    public Optional<Room> findById(Long number) {
        return roomRepository.findById(number);
    }

    /**
     * 챌린지 삭제  **방장만 add() 적용해야 함
     */
    @Transactional
    public void deleteRoom(Long roomNumber) {
        //엔티티 조회
        Room room = roomRepository.findById(roomNumber).get();
        RoomMember roomMember = roomMemberRepository.findByMemberNumber(roomNumber);


        //챌린지 삭제
        roomRepository.deleteById(roomNumber);

        //roomCount +1
        roomMember.add();
    }

    /**
     * 챌린지 실패  **방장만 add() 적용해야 함
     */
    @Transactional
    public void failRoom(Long roomNumber) {
        //엔티티 조회
        Room room = roomRepository.findById(roomNumber).get();

        List<RoomMember> roomMembers = room.getRoomMembers();
        int failCount = room.getFailCount();
        int passCount = room.getPassCount();

        if (passCount > failCount) {
            for (RoomMember roomMember : roomMembers) {
                roomRepository.deleteById(roomNumber);
            }
        }

        //roomCount +1
        RoomMember roomMember = roomMemberRepository.findByMemberNumber(roomNumber);
        roomMember.add();

    }

    /**
     * 챌린지 갯수 검색
     */
    public int findRoomCount(Long memberNumber) {
        RoomMember roomMember = roomMemberRepository.findByMemberNumber(memberNumber);
        int roomCount = roomMember.getCount();
        return roomCount;
    }


}
