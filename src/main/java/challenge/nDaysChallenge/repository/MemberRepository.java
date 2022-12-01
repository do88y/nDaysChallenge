package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    //이메일로 유저 찾기
    Optional<Member> findById(String id);

    //닉네임으로 유저찾기
    Optional<Member> findByNickname(String nickname);

    //중복 가입 방지
    boolean existsById(String id);

    //중복 닉네임 방지
    boolean existsByNickname(String nickname);

    //fetch join
    @Query("select m from Member m join fetch m.singleRooms")
    public List<SingleRoom> findAllWithSingleRoomsFetchJoin();

    Member findByNumber(Long memberNumber);


}