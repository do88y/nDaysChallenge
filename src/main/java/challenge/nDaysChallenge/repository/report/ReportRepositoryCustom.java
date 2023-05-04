package challenge.nDaysChallenge.repository.report;

import challenge.nDaysChallenge.dto.response.ReportResponseDto;

import java.util.List;

public interface ReportRepositoryCustom {
    List<ReportResponseDto> findReports(ReportSearch reportSearch);

}
