package challenge.nDaysChallenge.repository.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DajimRepository extends JpaRepository<Dajim, Long> {

    //룸멤버 (1~4명) 다짐 상세 조회
    List<Dajim> findAllByRoomNumber(Long roomNumber);

}
