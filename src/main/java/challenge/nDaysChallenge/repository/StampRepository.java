package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
=======
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
>>>>>>> 7addbbfed2e85f17c84ba27c61adc10d6384d2bb
import org.springframework.stereotype.Repository;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {

    @Query("select s from Stamp s where s.room = :room")
    public Stamp findByRoom(@Param("room") Room room);
}
