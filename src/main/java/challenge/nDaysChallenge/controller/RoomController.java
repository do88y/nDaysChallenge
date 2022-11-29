package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.RoomRequestDTO;
import challenge.nDaysChallenge.dto.response.RoomResponseDto;
import challenge.nDaysChallenge.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    /**
     * 챌린지 생성
     */
    @PostMapping("/challenge/create")
    public ResponseEntity<?> createRoom(@AuthenticationPrincipal User user,
                                        @RequestBody RoomRequestDTO roomRequestDTO) {

        Room room = roomService.createRoom(user, roomRequestDTO);
        RoomResponseDto savedRoom = RoomResponseDto.builder()
                .name(room.getName())
                .category(room.getCategory())
                .reward(room.getReward())
                .type(room.getType())
                .status(room.getStatus())
                .passCount(room.getPassCount())
                .totalDays(room.getPeriod().getTotalDays())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }


    /**
     * 챌린지 삭제
     */
    @DeleteMapping("/challenge/{user}/{challengeId}")
    public ResponseEntity<?> deleteRoom(@PathVariable("user") Long memberNumber,
                                        @PathVariable("challengeId") Long roomNumber) {

        roomService.deleteRoom(memberNumber, roomNumber);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
