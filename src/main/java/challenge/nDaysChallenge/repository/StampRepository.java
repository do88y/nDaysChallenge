package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {

    @Query("select s from Stamp s where s.room = :room")
    public Stamp findByRoom(@Param("room") Room room);

/*    @Query(value = "select * from group_room_stamps where room_number = :room and stamps_key = :key", nativeQuery = true)
    public Object findByRoomAndMember(@Param(value = "room") Long roomNumber,
                                      @Param(value = "key") String key);*/

}
