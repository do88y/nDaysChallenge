package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.Report;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.Room.DeleteRoomRequestDto;
import challenge.nDaysChallenge.dto.response.ReportResponseDto;
import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;
import challenge.nDaysChallenge.repository.ReportRepository;
import challenge.nDaysChallenge.repository.room.RoomSearch;
import challenge.nDaysChallenge.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.persistence.Tuple;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ReportRepository reportRepository;

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

    //챌린지 조회(id, status)
    @PostMapping("challenge/search")
    public String findResult(RoomSearch roomSearch, RedirectAttributes redirectAttributes) {

        List<Tuple> results = adminService.findRooms(roomSearch);
        List<AdminRoomResponseDto> rooms = new ArrayList<>();

        createRoomResponseDto(results, rooms);

        redirectAttributes.addFlashAttribute("challenges", rooms);
        redirectAttributes.addAttribute("status", true);

        log.info("status={}, id={}", roomSearch.getStatus(), roomSearch.getId());
        log.info("redirectAttributes.getFlashAttributes={}", redirectAttributes.getFlashAttributes());

        return "redirect:/admin/challenge";
    }

    //챌린지 삭제 - 여러개 동시에
    @PostMapping("challenge/delete")
    public String deleteRoom(DeleteRoomRequestDto dto, RedirectAttributes redirectAttributes) {

        adminService.deleteRoom(dto.getNumbers());

        redirectAttributes.addAttribute("status", true);

        dto.getNumbers().forEach(number -> log.info("number={}", number));

        return "redirect:/admin/challenge";
    }

    //신고 관리 페이지
    @GetMapping("/report")
    public String report(Model model) {

        List<Report> findAll = reportRepository.findAll();
        List<ReportResponseDto> reports = new ArrayList<>();
        for (Report one : findAll) {
            ReportResponseDto dto = ReportResponseDto.builder()
                    .report(one.getNumber())
                    .cause(one.getCause())
                    .isDajim(one.getIsDajim())
                    .content(one.getContent())
                    .dajim(one.getDajim().getNumber())
                    .build();
            reports.add(dto);
        }
        model.addAttribute("reports", reports);
        return "admin/report";
    }

    //신고 삭제
    @PostMapping("report/delete")
    public String deleteReport(DeleteRoomRequestDto dto) {

        adminService.deleteReport(dto.getNumbers());

        dto.getNumbers().forEach(number -> log.info("number={}", number));

        return "redirect:/admin/report";
    }


    //응답 dto 생성
    private static void createRoomResponseDto(List<Tuple> results, List<AdminRoomResponseDto> rooms) {
        for (Tuple result : results) {
            Room findRoom = result.get(0, Room.class);
            String memberId = result.get(1, String.class);

            AdminRoomResponseDto dto = AdminRoomResponseDto.builder()
                    .roomNumber(findRoom.getNumber())
                    .name(findRoom.getName())
                    .type(findRoom.getType().name())
                    .category(findRoom.getCategory().name())
                    .status(findRoom.getStatus().name())
                    .startDate(findRoom.getPeriod().getStartDate())
                    .endDate(findRoom.getPeriod().getEndDate())
                    .memberId(memberId)
                    .build();
            rooms.add(dto);
        }
    }
}
