package challenge.nDaysChallenge.repository.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.DajimComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DajimCommentRepository extends JpaRepository<DajimComment, Long> {
}
