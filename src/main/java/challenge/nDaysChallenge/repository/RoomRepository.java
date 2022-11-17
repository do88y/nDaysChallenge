package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

//    public void delete(Long Number) {
//        em.remove(Number);
//    }

