package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.Period;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import challenge.nDaysChallenge.dto.request.Room.DeleteRoomRequestDto;
import challenge.nDaysChallenge.dto.response.room.RoomResponseDto;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.RoomSearch;
import challenge.nDaysChallenge.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final RoomRepository roomRepository;

    //메인페이지
    @GetMapping
    public String main() {
        return "admin/main";
    }

    //메뉴
    @GetMapping("/menu")
    public String menu() {
        return "admin/menu";
    }

    @GetMapping("/challenge")
    public String challenge() {
        return "admin/challenge";
    }

    //챌린지 조회(id, status) - 현재 개인 챌린지만
    @PostMapping("challenge/search")
    public String findResult(RoomSearch roomSearch, RedirectAttributes redirectAttributes) {

        List<Room> findRooms = adminService.findRooms(roomSearch);
        List<RoomResponseDto> rooms = new ArrayList<>();
        for (Room findRoom : findRooms) {
            RoomResponseDto responseDto = RoomResponseDto.builder()
                    .roomNumber(findRoom.getNumber())
                    .name(findRoom.getName())
                    .type(findRoom.getType().name())
                    .category(findRoom.getCategory().name())
                    .status(findRoom.getStatus().name())
                    .startDate(findRoom.getPeriod().getStartDate())
                    .endDate(findRoom.getPeriod().getEndDate())
                    .build();
            rooms.add(responseDto);
        }

        redirectAttributes.addFlashAttribute("challenges", rooms);
        redirectAttributes.addAttribute("status", true);

        log.info("status={}, id={}", roomSearch.getStatus(), roomSearch.getId());
        log.info("redirectAttributes.getFlashAttributes={}", redirectAttributes.getFlashAttributes());

        return "redirect:/admin/challenge";
    }


    //챌린지 삭제 - 여러개 동시에
    @PostMapping("challenge/delete")
    public String delete(DeleteRoomRequestDto dto, RedirectAttributes redirectAttributes) {

        adminService.deleteRoom(dto.getNumbers());

        redirectAttributes.addAttribute("status", true);

        dto.getNumbers().forEach(number -> log.info("number={}", number));

        return "redirect:/admin/challenge";
    }
}
