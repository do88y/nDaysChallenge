package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {



    public RoomMember findByMemberNumber(Long member);
}
