package challenge.nDaysChallenge.repository.report;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportSearch {

    private Long dajimNumber;

    public ReportSearch(Long dajimNumber) {
        this.dajimNumber = dajimNumber;
    }
}
