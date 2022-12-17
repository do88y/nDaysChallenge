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

    //수정/삭제할 이모션 불러오기
    @Query("select e from Emotion e " +
            "where e.dajim.number = :dajimNumber " +
            "and e.member.number = :memberNumber")
    Optional<Emotion> findByEmotionNumber(@Param("dajimNumber")Long dajimNumber, @Param("memberNumber")Long memberNumber);

}
