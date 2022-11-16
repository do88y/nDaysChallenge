package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.*;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.RoomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

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
<<<<<<< HEAD
        Member member1 = new Member(1L, "aaa", "123","asdf", 1, Authority.ROLE_USER);
=======
        Member member1 = new Member("aaa", "123", Authority.ROLE_USER);
        Member member2 = new Member("bbb", "b4654bb", Authority.ROLE_USER);
        Member member3 = new Member("ccc", "6575", Authority.ROLE_USER);
        Member member4 = new Member("ddd", "bb324b", Authority.ROLE_USER);
>>>>>>> dbc2e4c5690ae87e879269700ffd1ca1a038eb2f

        Period period = new Period(5L);
        Room room = new Room("기상", period, Category.ROUTINE);

        //when
        Room createRoom = Room.builder()
                .name("기상")
                .category(Category.ROUTINE)
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