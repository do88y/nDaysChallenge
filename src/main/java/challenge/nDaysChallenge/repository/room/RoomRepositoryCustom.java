package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;

import javax.persistence.Tuple;
import java.util.List;

public interface RoomRepositoryCustom {

    List<AdminRoomResponseDto> findSingleRoomAdmin(RoomSearch roomSearch);

    List<AdminRoomResponseDto> findGroupRoomAdmin(RoomSearch roomSearch);
}