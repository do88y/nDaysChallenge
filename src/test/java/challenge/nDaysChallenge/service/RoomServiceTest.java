package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Room;
import challenge.nDaysChallenge.repository.RoomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RoomServiceTest {

    @Autowired RoomService roomService;
    @Autowired RoomRepository roomRepository;

    @Test
    public void 챌린지_생성() throws Exception {
        //given

        //when

        //then
    }
}