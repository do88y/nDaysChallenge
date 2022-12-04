package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long > {


    Relationship findByUserAndFriend(Member user, Member friend);





    //memberNumber로 수락상태인 관계, 챌린지 5개 이하만 검색-> 그룹 챌린지 멤버 후보에 뿌리기
    public List<Relationship> findRelationshipByUserAndStatus(Member member, RelationshipStatus status);

}