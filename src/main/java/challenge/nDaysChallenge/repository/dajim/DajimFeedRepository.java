package challenge.nDaysChallenge.repository.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.security.UserDetailsImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@Repository
public interface DajimFeedRepository extends JpaRepository<Dajim, Long> { //피드 전체 다짐 조회
    //전체 다짐 조회 - 내가 속한 그룹들이거나 open이 PUBLIC 일 때
    @Query("select d from Dajim d where d.member = :member or d.open = 'PUBLIC'")
    List<Dajim> findAllByMemberAndOpen(Member member);




}
