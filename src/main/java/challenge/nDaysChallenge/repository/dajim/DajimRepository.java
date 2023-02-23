package challenge.nDaysChallenge.repository.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.room.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DajimRepository extends JpaRepository<Dajim, Long> {

    @Override
    <S extends Dajim> S save(S entity);

    Optional<Dajim> findByNumber(Long dajimNumber);

    @Query("SELECT d FROM Dajim d WHERE d.member.number = :memberNumber AND d.room.number = :roomNumber")
    Optional<Dajim> findByMemberNumberAndRoomNumber(@Param("memberNumber") Long memberNumber, @Param("roomNumber") Long roomNumber);

    @Query("SELECT d FROM Dajim d WHERE d.member.nickname = :nickname")
    Optional<List<Dajim>> findAllByMemberNickname(@Param("nickname") String nickname);

    //챌린지 룸 다짐 조회 - 룸멤버 (1~4명) 다짐 상세 조회
    @Query("SELECT d FROM Dajim d JOIN d.room r" +
            " WHERE r.number = :roomNumber" +
            " ORDER BY d.updatedDate")
    Optional<List<Dajim>> findAllByRoomNumber(@Param("roomNumber") Long roomNumber);

    //피드 다짐 조회 - open=PUBLIC인 다짐
//    @Query("SELECT d FROM Dajim d WHERE d.open = 'PUBLIC' ORDER BY d.updatedDate DESC")
//    Optional<List<Dajim>> findAllByOpen();

    Page<Dajim> findAllByOpen(Open open, Pageable pageable);

}
