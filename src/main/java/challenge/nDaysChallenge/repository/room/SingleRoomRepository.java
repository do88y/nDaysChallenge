package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SingleRoomRepository extends JpaRepository<SingleRoom, Long> {

//    @Query(value = "select r from Room r where r.")
//    public List<SingleRoom> findByMember();
}
