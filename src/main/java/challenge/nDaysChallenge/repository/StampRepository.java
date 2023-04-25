package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StampRepository extends JpaRepository<Stamp, Long> {

    @Query("select s from Stamp s where s.room = :room")
    Stamp findByRoom(@Param("room") Room room);

    @Query("select s from Stamp s where s.room = :room")
    List<Stamp> findByGroupRoom(@Param("room") Room room);

}
