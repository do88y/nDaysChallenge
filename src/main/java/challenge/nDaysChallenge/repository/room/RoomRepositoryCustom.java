package challenge.nDaysChallenge.repository.room;

import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomRepositoryCustom {

    Page<AdminRoomResponseDto> findRoomAdmin(RoomSearch roomSearch, Pageable pageable);
}