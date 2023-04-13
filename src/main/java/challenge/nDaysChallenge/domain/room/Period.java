package challenge.nDaysChallenge.domain.room;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Period {

    private LocalDate startDate;
    private LocalDate endDate;
    private Long totalDays;


    //==생성 메서드==//
    public Period(LocalDate startDate, Long totalDays) {
        this.startDate = startDate;
        this.endDate = startDate.plusDays(totalDays-1);
        this.totalDays = totalDays;
    }
}