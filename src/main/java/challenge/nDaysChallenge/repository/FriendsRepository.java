package challenge.nDaysChallenge.repository;


import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import challenge.nDaysChallenge.vo.FriendsVO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendsRepository extends JpaRepository<FriendsVO, String> {

    //relationship status에 의거해 만들어진 리스트//
    public List<FriendsVO> selectFriends();

    public void insertFriends(FriendsVO vo);

    public void deleteFriendsVO(FriendsVO vo);


}
