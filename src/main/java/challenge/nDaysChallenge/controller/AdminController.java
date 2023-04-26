package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.Report;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.dto.request.Room.DeleteRoomRequestDto;
import challenge.nDaysChallenge.dto.request.jwt.LoginRequestDto;
import challenge.nDaysChallenge.dto.response.ReportResponseDto;
import challenge.nDaysChallenge.dto.response.jwt.TokenResponseDto;
import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;
import challenge.nDaysChallenge.jwt.TokenProvider;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.report.ReportRepository;
import challenge.nDaysChallenge.repository.report.ReportSearch;
import challenge.nDaysChallenge.repository.room.RoomSearch;
import challenge.nDaysChallenge.service.AdminService;
import challenge.nDaysChallenge.service.jwt.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final ReportRepository reportRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    //메인페이지
    @GetMapping
    public String main(Model model) {
        model.addAttribute("loginRequestDto", new LoginRequestDto());
        return "admin/main";
    }

    //관리자 로그인
    @PostMapping("/login")
    public String login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        // 토큰 생성
        TokenResponseDto tokenResponseDto = authService.login(loginRequestDto);

        Cookie cookie = new Cookie(
                "access_token",
                tokenResponseDto.getAccessToken());

        cookie.setPath("/admin/menu");
        cookie.setMaxAge(Integer.MAX_VALUE);

        response.addCookie(cookie);

        return "redirect:/admin/menu";
    }

    //메뉴
    @GetMapping("/menu")
    public String menu() {
        return "admin/menu";
    }

    //챌린지 관리자 페이지
    @GetMapping("/challenge")
    public String challenge() {
        return "admin/challenge";
    }

    //챌린지 조회(id, status)
    @PostMapping("challenge/search")
    public String findRoomResult(RoomSearch roomSearch, Pageable pageable,
                                 Model model, RedirectAttributes redirectAttributes) {

        Page<AdminRoomResponseDto> results = adminService.findRooms(roomSearch, pageable);

        redirectAttributes.addFlashAttribute("challenges", results);
        redirectAttributes.addAttribute("status", true);

        model.addAttribute("pages", results);
        model.addAttribute("maxPage", 5);

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

    @PostMapping("/report")
    public String reportSearch(ReportSearch reportSearch, Model model) {

        List<ReportResponseDto> reports = reportRepository.findReports(reportSearch);

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
