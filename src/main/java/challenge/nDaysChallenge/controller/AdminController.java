package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.dto.request.jwt.LoginRequestDto;
import challenge.nDaysChallenge.dto.response.AdminLoginDto;
import challenge.nDaysChallenge.service.AdminService;
import challenge.nDaysChallenge.dto.request.Room.DeleteRoomRequestDto;
import challenge.nDaysChallenge.dto.response.ReportResponseDto;
import challenge.nDaysChallenge.repository.report.ReportSearch;
import challenge.nDaysChallenge.domain.Report;
import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;
import challenge.nDaysChallenge.repository.report.ReportRepository;
import challenge.nDaysChallenge.repository.room.RoomSearch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
@Secured("ROLE_ADMIN")
public class AdminController {

    private final AdminService adminService;
    private final ReportRepository reportRepository;

    //로그인 페이지
    @GetMapping
    public String login(Model model,
                        LoginRequestDto loginRequestDto,
                        CsrfToken csrfToken) {
        model.addAttribute("loginRequestDto", loginRequestDto);
        model.addAttribute("_csrf", csrfToken);
        return "admin/login";
    }

    //관리자 로그인
    @PostMapping
    public String login(LoginRequestDto loginRequestDto, HttpSession session) {

        AdminLoginDto memberDto = adminService.login(loginRequestDto);
        session.setAttribute("id", memberDto.getId());
        session.setAttribute("auth", memberDto.getAuth());

        log.info("auth={}", memberDto.getAuth());

        return "redirect:/admin/menu";
    }

    //메뉴
    @GetMapping("/menu")
    public String menu() {
        return "admin/menu";
    }

    /**
     * 챌린지
     */
    //챌린지 관리자 페이지
    @GetMapping("/challenge")
    public String challenge() {
        return "admin/challenge";
    }

    //챌린지 조회(id, status)
    @PostMapping("/challenge/search")
    public String findRoomResult(RoomSearch roomSearch, Pageable pageable,
                                 Model model, RedirectAttributes redirectAttributes) {

        Page<AdminRoomResponseDto> results = adminService.findRooms(roomSearch, pageable);

        redirectAttributes.addAttribute("status", true);

        model.addAttribute("challenges", results);
        model.addAttribute("pages", results);
        model.addAttribute("maxPage", 5);

        log.info("status={}, id={}", roomSearch.getStatus(), roomSearch.getId());
        log.info("redirectAttributes.getFlashAttributes={}", redirectAttributes.getFlashAttributes());

        return "redirect:/admin/challenge";
    }

    //챌린지 삭제 - 여러개 동시에
    @PostMapping("/challenge/delete")
    public String deleteRoom(DeleteRoomRequestDto dto, RedirectAttributes redirectAttributes) {

        adminService.deleteRoom(dto.getNumbers());

        redirectAttributes.addAttribute("status", true);

        dto.getNumbers().forEach(number -> log.info("number={}", number));

        return "redirect:/admin/challenge";
    }

    /**
     * 신고
     */
    //신고 관리 페이지
    @GetMapping("/report")
    public String report(Model model) {

        List<Report> findAll = reportRepository.findAll();
        List<ReportResponseDto> reports = new ArrayList<>();
        for (Report one : findAll) {
            ReportResponseDto dto = ReportResponseDto.builder()
                    .report(one.getNumber())
                    .cause(one.getCause())
                    .content(one.getContent())
                    .dajim(one.getDajim().getNumber())
                    .build();
            reports.add(dto);
        }
        model.addAttribute("reports", reports);
        return "admin/report";
    }

    @PostMapping("/report")
    public String reportSearch(ReportSearch reportSearch, Model model) {

        List<ReportResponseDto> reports = reportRepository.findReports(reportSearch);

        model.addAttribute("reports", reports);
        return "admin/report";
    }

    //신고 삭제
    @PostMapping("/report/delete")
    public String deleteReport(DeleteRoomRequestDto dto) {

        adminService.deleteReport(dto.getNumbers());

        dto.getNumbers().forEach(number -> log.info("number={}", number));

        return "redirect:/admin/report";
    }


    //응답 dto 생성
/*    private static void createRoomResponseDto(List<AdminRoomResponseDto> results, List<AdminRoomResponseDto> rooms) {
        for (AdminRoomResponseDto result : results) {
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
    }*/
}
