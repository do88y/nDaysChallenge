package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.GroupRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupRoomRepository extends JpaRepository<GroupRoom, Long> {

    @Query(value = "select g from GroupRoom g join fetch g.roomMemberList")
    List<GroupRoom> findAllWithRoomMemberUsingFetchJoin();

    //진행중인 그룹 챌린지
    @Query(value = "select g from GroupRoom g" +
            " left join RoomMember rm" +
            " on g.number = rm.room.number" +
            " where rm.member = :member" +
            " and g.status = 'CONTINUE'")
    List<GroupRoom> findGroupRooms(@Param("member")Member member);

    //완료 그룹 챌린지
    @Query(value = "select g from GroupRoom g" +
            " left join RoomMember rm" +
            " on g.number = rm.room.number" +
            " where rm.member = :member" +
            " and g.status = 'END'")
    List<GroupRoom> finishedGroupRoom(@Param("member")Member member);

}
