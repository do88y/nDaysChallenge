package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SingleRoomRepository extends JpaRepository<SingleRoom, Long> {

    @Query(value = "select * from SingleRoom where member = :member", nativeQuery = true)
    List<SingleRoom> findSingleRooms(@Param("member") Long member);
}
