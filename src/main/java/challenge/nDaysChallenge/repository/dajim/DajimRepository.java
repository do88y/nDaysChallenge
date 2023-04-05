package challenge.nDaysChallenge.repository.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.service.dajim.CustomSliceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DajimRepository extends JpaRepository<Dajim, Long> {

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

    Slice<Dajim> findByOpen(Open open, Pageable pageable);

}
