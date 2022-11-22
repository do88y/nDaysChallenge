package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @DeleteMapping("/challenge/{user}/{challengeId}")
    public String deleteRoom(@PathVariable() Long user, @PathVariable Long challengeId) {

        roomService.deleteGroupRoom(user, challengeId);
        return "redirect:/challenge";
    }
}
