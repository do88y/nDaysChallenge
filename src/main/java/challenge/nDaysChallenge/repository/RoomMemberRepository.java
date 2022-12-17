package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {


    public RoomMember findByMemberAndRoom(Member member, Room room);

    public Set<RoomMember> findByRoomNumber(Long room);

}
