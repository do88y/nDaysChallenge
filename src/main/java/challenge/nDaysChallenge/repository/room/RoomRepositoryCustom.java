package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.SingleRoom;

import java.util.List;

public interface RoomRepositoryCustom {

    public List<SingleRoom> findSingleRoomAdmin(Member member, Room room);
}
