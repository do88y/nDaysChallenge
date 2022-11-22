package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.*;
import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.Period;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RoomServiceTest {

    @Autowired RoomRepository roomRepository;
    @Autowired RoomMemberRepository roomMemberRepository;
    @Autowired RoomService roomService;
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;


    @Test
    public void 개인_챌린지_생성() throws Exception {
        //given
        Period period = new Period(30L);

        //when
        Long newRoom = roomService.singleRoom(1L, "기상", period, Category.ROUTINE, 2);
/*        Room createRoom = Room.builder()
                .name("기상")
                .period(period)
                .category(Category.ROUTINE)
                .passCount(2)
                .build();*/

        //then
        Room room = roomRepository.findById(newRoom).get();
        Assertions.assertThat(room.getNumber()).isEqualTo(newRoom);
    }

    @Test
    public void 챌린지_삭제() throws Exception {
        //given

        //when

        //then
    }

}