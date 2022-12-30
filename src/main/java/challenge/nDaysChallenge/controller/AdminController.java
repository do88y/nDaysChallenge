package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.Room.DeleteRoomRequestDto;
import challenge.nDaysChallenge.dto.response.Room.RoomResponseDto;
import challenge.nDaysChallenge.repository.room.RoomSearch;
import challenge.nDaysChallenge.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;


    //챌린지 조회(id, status) - 현재 개인 챌린지만
    @GetMapping("admin/challenge/search")
    public ResponseEntity<?> findRooms(@RequestBody RoomSearch roomSearch) {

        List<Room> rooms = adminService.findRooms(roomSearch);
        List<RoomResponseDto> list = new ArrayList<>();

        for (Room room : rooms) {
            RoomResponseDto roomResponseDto = RoomResponseDto.builder()
                    .roomNumber(room.getNumber())
                    .name(room.getName())
                    .category(room.getCategory().name())
                    .reward(room.getReward())
                    .type(room.getType().name())
                    .status(room.getStatus().name())
                    .passCount(room.getPassCount())
                    .totalDays(room.getPeriod().getTotalDays())
                    .startDate(room.getPeriod().getStartDate())
                    .endDate(room.getPeriod().getEndDate())
                    .build();
            list.add(roomResponseDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(list);
    }

    //챌린지 삭제 - 여러개 동시에
    @DeleteMapping("/admin/challenge/delete")
    public ResponseEntity<?> delete(@RequestBody DeleteRoomRequestDto dto) {

        adminService.deleteRoom(dto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

}
