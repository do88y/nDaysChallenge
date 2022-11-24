package challenge.nDaysChallenge.domain;

import challenge.nDaysChallenge.domain.room.Period;
import org.junit.Test;


public class PeriodTest {


    @Test
    public void 챌린지_기간() throws Exception {
        //given
        Period period = new Period(30L);
        //when

        //then
        System.out.println("period.getEndDate = " + period.getEndDate());

    }
}