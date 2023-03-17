package challenge.nDaysChallenge.repository.dajim;

import challenge.nDaysChallenge.domain.dajim.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmotionRepository extends JpaRepository<Emotion, Long> {

    //수정/삭제할 이모션 불러오기
    @Query("select e from Emotion e " +
            "where e.dajim.number = :dajimNumber " +
            "and e.member.number = :memberNumber")
    Optional<Emotion> findByDajimAndMember(@Param("dajimNumber")Long dajimNumber, @Param("memberNumber")Long memberNumber);

    @Query("SELECT e FROM Emotion e WHERE e.member.id = :memberId")
    Optional<List<Emotion>> findAllByMemberId(@Param("memberId") String memberId);

}
