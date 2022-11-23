package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.room.GroupRoom;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.service.RoomService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {


}
