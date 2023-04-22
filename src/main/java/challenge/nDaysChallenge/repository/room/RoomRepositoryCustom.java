package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomRepositoryCustom {

    List<AdminRoomResponseDto> findRoomAdmin(RoomSearch roomSearch, Pageable pageable);
}