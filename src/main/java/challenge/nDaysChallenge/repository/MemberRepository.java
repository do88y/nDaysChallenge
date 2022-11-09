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
    public Member saveId(Member memberId){
        em.persist(memberId.getId());
        return memberId;
    }

    //비밀번호 저장//
    public Member savePw(Member memberPw){
        em.persist(memberPw.getPw());
        return em.find(Member.class,memberPw);
    }

    //닉네임 저장//
    public Member saveNickName(Member memberNickname){
        em.persist(memberNickname.getNickname());
        return memberNickname;
    }

    //프로필이미지 저장//
    public Member saveProfile(Member memberImage){
        em.persist(memberImage.getImage());
        return em.find(Member.class, memberImage);
    }

    //멤버 조회(단건)//
    public Member findOneId(Long id){
        return em.find(Member.class, id);
    }

    //멤버조회(닉네임으로)//
    public Member findNickname(String nickname){
        return em.find(Member.class, nickname);
    }







}
