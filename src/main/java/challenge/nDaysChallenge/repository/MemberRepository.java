package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    //멤버 Id(email)저장//
    public void saveId(Member memberId){
        em.persist(memberId.getId());
    }

    //비밀번호 저장//
    public void savePw(Member memberPw){
        em.persist(memberPw.getPw());
    }

    //닉네임 저장//
    public void saveNickName(Member memberNicname){
        em.persist(memberNicname.getNickname());
    }



    //




}
