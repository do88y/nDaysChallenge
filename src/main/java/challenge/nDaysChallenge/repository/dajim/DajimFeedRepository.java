package challenge.nDaysChallenge.repository.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Repository
public interface DajimFeedRepository extends JpaRepository<Dajim, Long> { //피드 전체 다짐 조회
    //전체 다짐 조회 - 같은 룸 멤버이거나 open이 PUBLIC 일 때
    @Query("select d from Dajim d where d.roomNumber = :roomNumber or d.open = 'PUBLIC'")
    List<Dajim> findAllByRoomNumberAndOpen(Long roomNumber);

    //다짐별 좋아요/댓글
    List<String> findAllByDajimandMember(Dajim dajim, Member member);

}
