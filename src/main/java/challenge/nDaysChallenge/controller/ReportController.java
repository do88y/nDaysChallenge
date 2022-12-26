package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.Report;
import challenge.nDaysChallenge.dto.request.ReportRequestDto;
import challenge.nDaysChallenge.dto.response.ReportResponseDto;
import challenge.nDaysChallenge.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    //신고 생성
    @PostMapping("/feed/report")
    public ResponseEntity<?> report(@RequestBody ReportRequestDto dto) {

        Report report = reportService.report(dto.getDajim(), dto.getCause(), dto.getIsDajim(), dto.getContent());
        ReportResponseDto savedReport = ReportResponseDto.builder()
                .report(report.getNumber())
                .cause(report.getCause())
                .isDajim(report.getIsDajim())
                .content(report.getContent())
                .dajim(report.getDajim().getNumber())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
    }
}
