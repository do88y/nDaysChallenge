package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
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

    public void saveMemberRoom(RoomMember roomMember) {
        em.persist(roomMember);
    }

    public RoomMember findMemberRoom(Member memberNumber) {
        return em.find(RoomMember.class, memberNumber);
    }

/*
    public MemberRoom findCount(Long memberNumber) {
        return em.createQuery("select mr.count from MemberRoom mr where mr.member = :memberNumber", MemberRoom.class)
                .setParameter("memberNumber", memberNumber)
                .getSingleResult();

    }
*/


}
