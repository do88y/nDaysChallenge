package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {

    public Optional<Room> findByNumber(Long roomNumber);

}

