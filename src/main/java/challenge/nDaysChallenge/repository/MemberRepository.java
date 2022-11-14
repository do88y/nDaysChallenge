package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByNumber(Long number);

    Member findByNickname(String nickname);






}



