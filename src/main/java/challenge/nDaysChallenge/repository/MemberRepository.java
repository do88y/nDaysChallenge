package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Member;
<<<<<<< HEAD
import challenge.nDaysChallenge.domain.RoomMember;
import lombok.RequiredArgsConstructor;
=======
import org.springframework.data.jpa.repository.JpaRepository;
>>>>>>> 7fae07e2e7360b3da291e6ce5f9b51a51ba031a1
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    //아이디(이메일) 저장//
    void saveId(String id);

    //비밀번호 저장//
    void savePw(String pw);


    //닉네임 저장//
<<<<<<< HEAD
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
=======
    void saveNickname(String nickname);

    //이메일로 유저 찾기//
    Optional<Member> findById(String id);
>>>>>>> 7fae07e2e7360b3da291e6ce5f9b51a51ba031a1

    //중복 가입 방지//
    boolean existsById(String id);

}
