package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingleRoomRepository extends JpaRepository<SingleRoom, Long> {

<<<<<<< HEAD

}
=======
    //멤버로 개인 챌린지 갯수 조회
    public Long countByMember(Member member);

}
>>>>>>> bf644cc4706be7c3891929856e1caabeb79d3522
