package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    //특정 멤버의 모든 완료된 챌린지룸 확인
//    @Query("SELECT ")
//    List<Room> findAllGroupRoomByMember(Member member);
//
//    @Query("SELECT r FROM Room r WHERE r.")
//    List<Room> findAllSingleRoomByMember(Member member);

}

