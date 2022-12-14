package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SingleRoomRepository extends JpaRepository<SingleRoom, Long> {

    //진행중인 개인 챌린지
    @Query(value = "select s from SingleRoom s" +
                    " where s.member = :member" +
                    " and s.status = 'CONTINUE'")
    public List<SingleRoom> findSingleRooms(@Param("member") Member member);

    //완료 개인 챌린지r
    @Query(value = "select s from SingleRoom s" +
                    " where s.member = :member" +
                    " and s.status = 'END'")
    public List<SingleRoom> finishedSingleRooms(@Param("member") Member member);

}
