package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingleRoomRepository extends JpaRepository<SingleRoom, Long> {

    //멤버로 개인 챌린지 갯수 조회
    public Long countByMember(Member member);

}