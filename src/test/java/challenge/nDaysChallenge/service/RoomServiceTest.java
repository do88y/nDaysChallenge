package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.SingleRoomRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = false)
public class RoomServiceTest {

    @Autowired EntityManager em;
    @Autowired RoomRepository roomRepository;
    @Autowired RoomService roomService;
    @Autowired MemberRepository memberRepository;

    @DisplayName("개인 챌린지 단독")
    @Test
    public void 개인_챌린지_생성() throws Exception {
        //given
        Room room = SingleRoom.builder()
                .name("기상")
                .period(new Period(30L))
                .category(Category.ROUTINE)
                .type(RoomType.SINGLE)
                .passCount(2)
                .build();

        em.persist(room);

        //when
        Optional<Room> findRoom = roomRepository.findById(1L);


        //then
        assertThat(room.getNumber()).isEqualTo(findRoom.get().getNumber());
    }

    @DisplayName("단체 챌린지 단독")
    @Test
    public void 단체_챌린지_생성() throws Exception {
        //given
        Member member1 = new Member("user1@naver.com", "12345", "nick1", 1, 4, Authority.ROLE_USER);
        Member member2 = new Member("user2@naver.com", "12345", "nick2", 1, 4, Authority.ROLE_USER);

        //when

        //then
    }

    @DisplayName("singleRoom 메서드 전체")
    @Test
    public void singleRoom_test() throws Exception {
        //given
        Member member = new Member("user@naver.com", "12345", "nick", 1, 4, Authority.ROLE_USER);

        em.persist(member);

        //when
        Long roomNumber = roomService.singleRoom(1L, "기상", period, Category.ROUTINE, 2);

        //then
        Optional<Room> findSingleRoom = roomRepository.findById(1L);
        assertThat(findSingleRoom.get().getNumber()).isEqualTo(roomNumber);
    }

    @DisplayName("챌린지 삭제")
    @Test
    public void 챌린지_삭제() throws Exception {
        //given
        Room room = new Room("기상", period, Category.ROUTINE, RoomType.GROUP, 2);

        em.persist(room);

        //when
        roomRepository.deleteById(1L);

        //then
        long count = roomRepository.count();
        assertThat(count).isEqualTo(0);
    }

    Period period = new Period(30L);

}