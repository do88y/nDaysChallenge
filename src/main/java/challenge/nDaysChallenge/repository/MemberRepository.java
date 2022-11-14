package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    //아이디(이메일) 저장//
    void saveId(String id);

    //비밀번호 저장//
    void savePw(String pw);


    //닉네임 저장//
    void saveNickname(String nickname);

    //이메일로 유저 찾기//
    Optional<Member> findById(String id);

    //중복 가입 방지//
    boolean existsById(String id);

}
