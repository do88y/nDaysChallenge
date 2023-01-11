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

    //==값 타입 비교==//  인스턴스가 아닌 값이 같은지 비교해야 함
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Period period = (Period) o;
        return Objects.equals(getStartDate(), period.getStartDate()) && Objects.equals(getEndDate(), period.getEndDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStartDate(), getEndDate());
    }
}