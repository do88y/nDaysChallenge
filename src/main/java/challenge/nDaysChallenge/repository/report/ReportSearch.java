package challenge.nDaysChallenge.repository.report;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportSearch {

    private Boolean isDajim;
    private Long dajimNumber;

    public Boolean getIsDajim() {
        return isDajim;
    }

    public ReportSearch(Boolean isDajim, Long dajimNumber) {
        this.isDajim = isDajim;
        this.dajimNumber = dajimNumber;
    }
}
