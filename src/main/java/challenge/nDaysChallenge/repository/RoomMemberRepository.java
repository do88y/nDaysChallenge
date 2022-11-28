package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {


    public RoomMember findByMemberNumber(Long member);

    public List<RoomMember> findByRoomNumber(Long room);

    //멤버로 그룹챌린지 갯수 조회
    public Long countByMember(Member member);

}
