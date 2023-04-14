package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;

import javax.persistence.Tuple;
import java.util.List;

public interface RoomRepositoryCustom {

    public List<Tuple> findSingleRoomAdmin(RoomSearch roomSearch);

    public List<Tuple> findGroupRoomAdmin(RoomSearch roomSearch);
}