package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
<<<<<<< HEAD
=======
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
>>>>>>> 7addbbfed2e85f17c84ba27c61adc10d6384d2bb
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long > {




    Relationship findByUserAndFriend(Member user, Member friend);



    @Query("select r from Relationship r where r.user=:user and r.status='ACCEPT'")
    public List<Relationship> findRelationshipByUserAndStatus(@Param("user")Member member);

}