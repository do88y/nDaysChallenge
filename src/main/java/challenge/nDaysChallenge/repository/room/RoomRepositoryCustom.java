package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.persistence.Tuple;
import java.util.List;

public interface RoomRepositoryCustom {

    Page<AdminRoomResponseDto> findSingleRoomAdmin(RoomSearch roomSearch, Pageable pageable);

    List<AdminRoomResponseDto> findGroupRoomAdmin(RoomSearch roomSearch);
}