package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.room.GroupRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GroupRoomRepository extends JpaRepository<GroupRoom, Long> {

    @Query("select g from GroupRoom g join fetch g.roomMemberList")
    public List<GroupRoom> findAllWithRoomMemberUsingFetchJoin();
}
