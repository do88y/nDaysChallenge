package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

<<<<<<< HEAD

}
=======
    public void delete(Long Number) {
        em.remove(Number);
    }

}
>>>>>>> dbc2e4c5690ae87e879269700ffd1ca1a038eb2f
