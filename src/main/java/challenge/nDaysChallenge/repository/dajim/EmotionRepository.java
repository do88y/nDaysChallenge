package challenge.nDaysChallenge.repository.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Long> {

    //다짐넘버로 다짐 가져오기
    @Query("select d from Dajim d where d.number=:dajimNumber")
    Dajim findByDajimNumber(Long dajimNumber);

}
