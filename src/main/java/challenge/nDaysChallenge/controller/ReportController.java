package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.Report;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.dto.request.ReportRequestDto;
import challenge.nDaysChallenge.dto.response.ReportResponseDto;
import challenge.nDaysChallenge.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    //신고 생성
    @PostMapping("/feed/report")
    public ResponseEntity<?> report(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                    @RequestBody ReportRequestDto dto) {

        checkLogin(memberAdapter.getMember());

        Report report = reportService.report(dto.getDajim(), dto.getCause(), dto.getIsDajim(), dto.getContent());
        ReportResponseDto savedReport = ReportResponseDto.builder()
                .report(report.getNumber())
                .cause(report.getCause())
                .isDajim(report.getIsDajim())
                .content(report.getContent())
                .dajim(report.getDajim().getNumber())
                .build();

        URI location = URI.create("/feed/report" + report.getNumber());

        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(savedReport);
    }

    //로그인 검증
    private void checkLogin(Member member) {
        if (member == null) {
            throw new RuntimeException("로그인한 멤버만 사용할 수 있습니다.");
        }
    }
}
