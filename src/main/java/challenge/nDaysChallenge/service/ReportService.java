package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Report;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.repository.report.ReportRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final DajimRepository dajimRepository;

    /**
     * 신고 생성
     */
    public Report report(Long dajimNumber, int cause, String content) {

        //엔티티 조회
        Optional<Dajim> findDajim = dajimRepository.findByNumber(dajimNumber);
        Dajim dajim = findDajim.orElseThrow(() -> new NoSuchElementException("해당 다짐이 없습니다. "));

        Report report = Report.createReport(dajim, cause, content);

        reportRepository.save(report);

        return report;
    }
}
