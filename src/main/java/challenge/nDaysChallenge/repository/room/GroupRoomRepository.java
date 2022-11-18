package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.room.GroupRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRoomRepository extends JpaRepository<GroupRoom, Long> {
}
