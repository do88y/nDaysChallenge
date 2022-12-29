package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.room.Room;

import java.util.List;

public interface RoomRepositoryCustom {

    public List<Room> findSingleRoomAdmin(RoomSearch roomSearch);
}
