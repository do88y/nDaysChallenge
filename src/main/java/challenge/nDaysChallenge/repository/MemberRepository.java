package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    //멤버 저장//
    public void save(Member member){
        em.persist(member);
    }



}
