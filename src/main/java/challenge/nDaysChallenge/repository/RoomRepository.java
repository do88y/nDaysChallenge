package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class RoomRepository {

    private final EntityManager em;  //스프링데이터JPA가 PersistenceContext 없이도 Autowired 되도록 지원

    public void save(Room room) {
        em.persist(room);  //이 때 Number 값이 생성되는 것이 보장 됨
    }

    public Room findOne(Long id) {
        return em.find(Room.class, id);
    }

    public List<Room> findAll() {
        return em.createQuery("select r from Room r", Room.class)
                .getResultList();
    }

}
