package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SingleRoomRepository extends JpaRepository<SingleRoom, Long> {

    //멤버의 개인 챌린지 찾는 메서드
    @Query(value = "select s from SingleRoom s where s.member = :member")
    List<SingleRoom> findSingleRooms(@Param("member") Member member);
}
