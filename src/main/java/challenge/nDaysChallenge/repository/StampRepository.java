package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {

    @Query("select s from Stamp s where s.room = :room")
    public Stamp findByRoom(@Param("room") Room room);

    @Query("select s from Stamp s where s.room = :room and s.member = :member")
    public Stamp findByRoomAndMember(@Param("room") Room room,
                                     @Param("member") Member member);

}
