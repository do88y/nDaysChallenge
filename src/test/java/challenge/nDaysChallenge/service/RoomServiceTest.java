package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.Period;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
public class RoomServiceTest {

    RoomRepository roomRepository;
    RoomMemberRepository roomMemberRepository;
    RoomService roomService;
    MemberService memberService;
    MemberRepository memberRepository;

    @DisplayName("개인 챌린지 생성")
    @Test
    public void 개인_챌린지_생성() throws Exception {
        //given
        Period period = new Period(30L);
        Long newRoom = roomService.singleRoom(1L, "기상", period, Category.ROUTINE, 2);

        //when
/*        Room createRoom = Room.builder()
                .name("기상")
                .period(period)
                .category(Category.ROUTINE)
                .passCount(2)
                .build();*/

        //then
        Room room = roomRepository.findById(newRoom).get();
        assertThat(room.getNumber()).isEqualTo(newRoom);
    }

    @DisplayName("단체 챌린지 생성")
    @Test
    public void 단체_챌린지_생성() throws Exception {
        //given

        //when

        //then
    }

    @DisplayName("챌린지 삭제")
    @Test
    public void 챌린지_삭제() throws Exception {
        //given
        Member member = new Member(1L, "asdf", "sdfs", "sf", 1, 4, Authority.ROLE_USER);
        Period period = new Period(30L);
        Long newRoom1 = roomService.singleRoom(1L, "기상", period, Category.ROUTINE, 2);

        //when
//        roomService.deleteRoom(newRoom1, member.getNumber());

        //then
        Room room = roomRepository.findById(newRoom1).get();
    }

}