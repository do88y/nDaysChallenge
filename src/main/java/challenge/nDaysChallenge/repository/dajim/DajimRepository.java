package challenge.nDaysChallenge.repository.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DajimRepository extends JpaRepository<Dajim, Long> {

    @Override
    <S extends Dajim> S save(S entity);

    //룸멤버 (1~4명) 다짐 상세 조회
    @Query("SELECT d FROM Dajim d JOIN d.room r" +
            " WHERE r.number = :roomNumber" +
            " ORDER BY d.updatedDate")
    Optional<List<Dajim>> findAllByRoomNumber(@Param("roomNumber") Long roomNumber);

    //룸 넘버로 룸 객체 찾기
    @Query("SELECT r FROM Room r WHERE r.number = :roomNumber")
    Optional<Room> findByRoomNumber(@Param("roomNumber") Long roomNumber);

    @Query("SELECT d FROM Dajim d WHERE d.number = :dajimNumber")
    Optional<Dajim> findByDajimNumber(@Param("dajimNumber") Long dajimNumber);

    @Query("SELECt d FROM Dajim d WHERE d.member.nickname = :nickname")
    Optional<List<Dajim>> findAllByMemberNickname(@Param("nickname") String nickname);

}
