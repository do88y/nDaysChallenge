package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.RoomType;
import challenge.nDaysChallenge.dto.request.RoomRequestDTO;
import challenge.nDaysChallenge.dto.response.RoomResponseDto;
import challenge.nDaysChallenge.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

/*    @PostMapping("/challenge/create")
    public ResponseEntity<?> createRoom(@RequestBody Long memberNumber, RoomRequestDTO roomRequestDTO) {

        Room room = roomService.createRoom(memberNumber, roomRequestDTO);
        RoomResponseDto savedRoom = RoomResponseDto.builder()
                .name(room.getName())
                .category(room.getCategory())
                .reward(room.getReward())
                .type(room.getType())
                .status(room.getStatus())
                .passCount(room.getPassCount())
                .totalDays(room.get)
                .build()
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }*/

    @DeleteMapping("/challenge/{user}/{challengeId}")
    public String deleteRoom(@PathVariable("user") Long memberNumber,
                             @PathVariable("challengeId") Long roomNumber) {

        roomService.deleteRoom(memberNumber, roomNumber);
        return "redirect:/challenge";
    }
}
