package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.GroupRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface GroupRoomRepository extends JpaRepository<GroupRoom, Long> {

    //진행중인 그룹 챌린지
    @Query(value = "select g from GroupRoom g" +
            " left join RoomMember rm" +
            " on g.number = rm.room.number" +
            " where rm.member = :member" +
            " and g.status = 'CONTINUE'")
    public List<GroupRoom> findGroupRooms(@Param("member") Member member);

    //완료 그룹 챌린지
    @Query(value = "select g from GroupRoom g" +
            " left join RoomMember rm" +
            " on g.number = rm.room.number" +
            " where rm.member = :member" +
            " and g.status = 'END'")
    public List<GroupRoom> finishedGroupRoom(@Param("member") Member member);

    @Query(value = "select g from GroupRoom g" +
            " left join RoomMember rm" +
            " on g.number = rm.room.number" +
            " where rm.member = :member")
    Optional<List<GroupRoom>> findAll(@Param("member") Member member);

}
