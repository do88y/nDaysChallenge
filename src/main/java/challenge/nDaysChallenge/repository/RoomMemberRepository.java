package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {


    public RoomMember findByMemberNumber(Long member);

    public List<RoomMember> findByRoomNumber(Long room);

}
