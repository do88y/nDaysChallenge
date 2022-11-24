package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.*;
import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.Period;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
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
    public void 챌린지_생성() throws Exception {
        //given
        Member member1 = new Member("aaa", "123","asdf", 1,4, Authority.ROLE_USER);


        Period period = new Period(5L);

        //when
        Room createRoom = Room.builder()
                .name("기상")
                .category(Category.ROUTINE)
                .passCount(0)
                .build();

        //then
    }

    @Test
    public void 챌린지_삭제() throws Exception {
        //given

        //when

        //then
    }

}