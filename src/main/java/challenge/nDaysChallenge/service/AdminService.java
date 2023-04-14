package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.RoomSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Tuple;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminService {

    private final RoomRepository roomRepository;

    //챌린지 조회(멤버 id, 챌린지 상태)
    public List<Tuple> findRooms(RoomSearch roomSearch) {

        List<Tuple> singleRoomResult = roomRepository.findSingleRoomAdmin(roomSearch);
        List<Tuple> groupRoomResult = roomRepository.findGroupRoomAdmin(roomSearch);
        List<Tuple> result = Stream.concat(
                singleRoomResult.stream(), groupRoomResult.stream()
                ).collect(Collectors.toList());
        return result;
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
