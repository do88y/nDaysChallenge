package challenge.nDaysChallenge.domain;

import org.junit.Test;

import static org.junit.Assert.*;


public class PeriodTest {


    @Test
    public void 챌린지_기간() throws Exception {
        //given
        Period period = new Period(5L);
        //when
        System.out.println("period.getEndDate = " + period.getEndDate());
        //then

    }
}