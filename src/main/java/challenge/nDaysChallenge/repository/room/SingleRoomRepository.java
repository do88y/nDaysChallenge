package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SingleRoomRepository extends JpaRepository<SingleRoom, Long> {

    //진행중인 개인 챌린지
    @Query(value = "select s from SingleRoom s" +
                    " where s.member = :member" +
                    " and s.status = 'CONTINUE'")
    List<SingleRoom> findSingleRooms(@Param("member") Member member);

    //완료 개인 챌린지
    @Query(value = "select s from SingleRoom s" +
                    " where s.member = :member" +
                    " and s.status = 'END'")
    List<SingleRoom> finishedSingleRooms(@Param("member") Member member);


    @Query(value = "delete from SingleRoom s where s.number = :number")
    SingleRoom deleteByNumber(@Param("number") Long number);
}
