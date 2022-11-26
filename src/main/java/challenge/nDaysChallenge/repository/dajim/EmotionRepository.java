package challenge.nDaysChallenge.repository.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Long> {

    //다짐넘버로 다짐 가져오기
    @Query("select d from Dajim d where d.number = :dajimNumber")
    Optional<Dajim> findByDajimNumberForEmotion(@Param("dajimNumber") Long dajimNumber);

    @Query("select e from Emotion e " +
            "where e.dajim.number = :dajimNumber " +
            "and e.member.number = :memberNumber")
    Optional<Emotion> findByEmotionNumber(@Param("dajimNumber")Long dajimNumber, @Param("memberNumber")Long memberNumber);

}
