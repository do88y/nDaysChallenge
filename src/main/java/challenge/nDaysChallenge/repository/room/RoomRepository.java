package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomRepositoryCustom {

    Optional<Room> findByNumber(Long roomNumber);

    @Query("select r.member from Room r where r.number = :number")
    Optional<Member> findMemberByRoomNumber(@Param("number") Long number);
}

