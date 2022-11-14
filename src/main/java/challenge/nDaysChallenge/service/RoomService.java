package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Room;
import challenge.nDaysChallenge.repository.RoomRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor  //final 붙은 필드로만 생성자를 만들어 줌
public class RoomService {

    private final RoomRepository roomRepository;

    /**
     * 챌린지 생성
     */
    @Transactional
    public Long createRoom(Room room) {
        roomRepository.save(room);
        return room.getNumber();
    }

    //챌린지 전체 조회
    public List<Room> findRooms() {
        return roomRepository.findAll();
    }

    //특정 챌린지 조회
    public Room findOne(Long roomNumber) {
        return roomRepository.findOne(roomNumber);
    }
}
