package challenge.nDaysChallenge.repository.member;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    //이메일로 유저 찾기
    Optional<Member> findById(String id);

    //닉네임으로 유저찾기
    Optional<Member> findByNickname(String nickname);

    //아이디 중복 체크
    boolean existsById(String id);

    //닉네임 중복 체크
    boolean existsByNickname(String nickname);

    Optional<Member> findByNumber(Long memberNumber);


}