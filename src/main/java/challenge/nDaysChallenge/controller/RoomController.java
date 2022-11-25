package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.dto.request.RoomRequestDTO;
import challenge.nDaysChallenge.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/challenge/create")
    public String createRoom(@RequestBody RoomRequestDTO roomRequestDTO) {

        return "redirect:/challenge";
    }

    @DeleteMapping("/challenge/{user}/{challengeId}")
    public String deleteRoom(@PathVariable Long user, @PathVariable Long challengeId) {

        roomService.deleteRoom(user, challengeId);
        return "redirect:/challenge";
    }
}
