package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship, Long > {

    @Query("select r from Relationship r where r.user.id=:myId and r.friend.id=:friendId")
    Relationship findByUserIdAndFriendId(String myId, String friendId);


    //친구 요청시 시행되는 쿼리//
    @Query("select r from Relationship r where r.user.id=:myId and r.status='REQUEST'")
    List<Relationship>findRelationshipByFriendAndStatus(@Param("myId") String myId);


    //친구 수락시 시행되는 쿼리//
    @Query("select r from Relationship r where r.user.id=:myId and r.status='ACCEPT'")
    List<Relationship> findRelationshipByUserAndStatus(@Param("myId") String myId);

}