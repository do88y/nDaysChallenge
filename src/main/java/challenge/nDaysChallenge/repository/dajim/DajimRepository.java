package challenge.nDaysChallenge.repository.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.room.Room;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DajimRepository extends JpaRepository<Dajim, Long> {

    //이미 있는 다짐인지 조회
    Dajim findByRoomAndMember(Room room, Member member);

    //룸멤버 (1~4명) 다짐 상세 조회
    @Query("SELECT d FROM Dajim d JOIN d.room r WHERE r.number=:roomNumber")
    List<Dajim> findAllByRoomNumber(Long roomNumber);

    //룸 넘버로 룸 객체 찾기
    @Query("SELECT r FROM Room r WHERE r.number=:roomNumber")
    Room findByRoomNumber(Long roomNumber);

    @Query("SELECT d FROM Dajim d WHERE d.number=:dajimNumber")
    Dajim findByDajimNumber(Long dajimNumber);

}
