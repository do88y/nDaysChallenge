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
    public String createRoom(@RequestBody Long memberNumber, RoomRequestDTO roomRequestDTO) {

//        roomService.singleRoom(memberNumber, roomRequestDTO);
        return "redirect:/challenge";
    }

    @DeleteMapping("/challenge/{user}/{challengeId}")
    public String deleteRoom(@PathVariable("user") Long memberNumber,
                             @PathVariable("challengeId") Long roomNumber) {

        roomService.deleteRoom(memberNumber, roomNumber);
        return "redirect:/challenge";
    }
}
