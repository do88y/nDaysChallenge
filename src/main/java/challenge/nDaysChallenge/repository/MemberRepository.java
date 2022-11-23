package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.Relationship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
=======
import java.util.List;
>>>>>>> refs/remotes/origin/develop
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByNumber(Long number);


    //이메일로 유저 찾기//
    Optional<Member> findById(String id);

    Member findByNickname(String nickname);

    Member findByPw(String pw);

    Member findByImage(int image);


    //중복 가입 방지//
    boolean existsById(String id);
<<<<<<< HEAD



    //중복 닉네임 방지//
    boolean existsByNickname(String nickname);

=======

    //중복 닉네임 방지//
    boolean existsByNickname(String nickname);


    //fetch join
    @Query("select m from Member m join fetch m.friends")
    public List<Relationship> findAllWithFriendsFetchJoin();
>>>>>>> refs/remotes/origin/develop

}

