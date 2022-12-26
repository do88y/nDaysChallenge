package challenge.nDaysChallenge.repository.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DajimFeedRepository extends JpaRepository<Dajim, Long> { //피드 다짐 조회
    //멤버가 속한 챌린지 내 다짐 or open=PUBLIC인 다짐
    @Query("SELECT d FROM Dajim d WHERE d.open = 'PUBLIC'")
    List<Dajim> findAllByOpen();
}
