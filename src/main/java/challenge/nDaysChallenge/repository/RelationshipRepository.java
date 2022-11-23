package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, String > {

    //memberNumber로 수락상태인 관계만 검색//
    Relationship findByUserNumberAndStatus(String member, RelationshipStatus relationshipStatus);


}
