package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SingleRoomRepository extends JpaRepository<SingleRoom, Long> {

    //진행 개인 챌린지
    @Query("select s from SingleRoom s" +
            " where s.member.id = :id" +
            " and s.status = 'CONTINUE'")
    List<SingleRoom> findSingleRooms(@Param("id") String id);

    //완료 개인 챌린지
    @Query("select s from SingleRoom s" +
            " where s.member.id = :id" +
                    " and s.status = 'END'")
    List<SingleRoom> finishedSingleRooms(@Param("id") String id);

    //전체 개인 챌린지
    @Query("select s from SingleRoom s" +
            " where s.member.id = :id")
    List<SingleRoom> findAll(@Param("id") String id);

    @Query("select s.stamp from SingleRoom s where s.member.id = :id")
    List<Stamp> findStampByMember(@Param("id") String id);

}
