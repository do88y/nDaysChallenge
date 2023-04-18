package challenge.nDaysChallenge.Room;

import challenge.nDaysChallenge.domain.room.Period;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;


public class PeriodTest {


    @Test
    public void 챌린지_기간() throws Exception {
        //given
        Period period = new Period(LocalDate.now(),30);
        //when

        //then
        System.out.println("period.getEndDate = " + period.getEndDate());
        Assertions.assertThat(period.getEndDate()).isEqualTo(LocalDate.now().plusDays(30L-1));
    }
}