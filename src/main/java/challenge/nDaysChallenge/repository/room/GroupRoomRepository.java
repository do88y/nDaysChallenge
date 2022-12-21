package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.GroupRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface GroupRoomRepository extends JpaRepository<GroupRoom, Long> {

    @Query(value = "select g from GroupRoom g join fetch g.roomMemberList")
    public List<GroupRoom> findAllWithRoomMemberUsingFetchJoin();

    @Query(value = "select g from GroupRoom g" +
            " left join RoomMember rm" +
            " on g.number = rm.room.number" +
            " where rm.member = :member")
    public List<GroupRoom> findByMember(@Param("member")Member member);
}
