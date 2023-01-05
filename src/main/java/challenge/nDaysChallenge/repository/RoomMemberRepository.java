package challenge.nDaysChallenge.repository;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.RoomMember;
import challenge.nDaysChallenge.domain.room.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface RoomMemberRepository extends JpaRepository<RoomMember, Long> {


    public RoomMember findByMemberAndRoom(Member member, Room room);

    public Set<RoomMember> findByRoom(Room room);

    @Query("SELECT r FROM RoomMember r WHERE r.member.nickname = :nickname")
    Optional<List<RoomMember>> findAllByMemberNickname(@Param("nickname") String nickname);

}
