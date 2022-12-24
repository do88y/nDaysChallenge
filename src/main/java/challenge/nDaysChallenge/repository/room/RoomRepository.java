package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.GroupRoom;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Override
    Optional<Room> findById(Long roomNumber);

}

