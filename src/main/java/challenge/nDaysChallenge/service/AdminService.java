package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.Room.DeleteRoomRequestDto;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.RoomSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final RoomRepository roomRepository;

    //챌린지 조회(멤버 id, 챌린지 상태)
    public List<Room> findRooms(RoomSearch roomSearch) {

        return roomRepository.findSingleRoomAdmin(roomSearch);
    }

    //여러 챌린지 삭제
    @Transactional
    public void deleteRoom(List<Long> numbers) {

        for (Long number : numbers) {
            Room room = roomRepository.findByNumber(number).orElseThrow(
                    () -> new NoSuchElementException("챌린지가 존재하지 않습니다."));
            roomRepository.delete(room);
        }
    }

}
