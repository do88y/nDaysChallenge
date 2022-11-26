package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
