package challenge.nDaysChallenge.repository.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DajimRepository extends JpaRepository<Dajim, Long> {

    //다짐 저장
    //@Override
    //<S extends Dajim> S save(S entity); //save() : insert & update 모두 수행

    //룸멤버 (1~4명) 다짐 상세 조회
    Optional<Dajim> findAllByRoomNumber(Long roomNumber);

    //피드 전체 다짐 조회
    //같은 룸 멤버이거나 open이 PUBLIC 일 때
    @Query("select d from Dajim d where d.roomNumber = :roomNumber or d.open = 'PUBLIC'")
    List<Dajim> findAllByRoomNumberAndOpen(Long roomNumber);

}
