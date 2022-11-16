package challenge.nDaysChallenge.domain;

import challenge.nDaysChallenge.repository.RoomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
public class PeriodTest {

//    @Autowired RoomRepository roomRepository;

    @Test
    public void 챌린지_기간() throws Exception {
        //given
        Period period = new Period(5L);
        Room room = new Room("기상", period, Category.ROUTINE);
        //when
//        Room savedId = roomRepository.save(room);
        System.out.println("period.getEndDate = " + period.getEndDate());
        //then
//        assertEquals(period, roomRepository.findById(savedId.getNumber()));
    }
}