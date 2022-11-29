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

    //챌린지 생성
    @PostMapping("/challenge/create")
    public ResponseEntity<?> createRoom(@AuthenticationPrincipal User user,
                                        @RequestBody RoomRequestDTO roomRequestDTO) {

        Room room = roomService.createRoom(user, roomRequestDTO);
<<<<<<< HEAD
=======

>>>>>>> cedc1d880e101d6df25ed2baaf8f9f2d210d442d
        RoomResponseDto savedRoom = RoomResponseDto.builder()
                .name(room.getName())
                .category(room.getCategory().name())
                .reward(room.getReward())
                .type(room.getType().name())
                .status(room.getStatus().name())
                .passCount(room.getPassCount())
                .totalDays(room.getPeriod().getTotalDays())
                .groupMembers(roomRequestDTO.getGroupMembers())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }


    //챌린지 삭제
    @DeleteMapping("/challenge/{challengeId}")
    public ResponseEntity<?> deleteRoom(@AuthenticationPrincipal User user,
                                        @PathVariable("challengeId") Long roomNumber) {

        roomService.deleteRoom(user, roomNumber);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
