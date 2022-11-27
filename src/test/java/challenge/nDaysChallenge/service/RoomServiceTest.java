package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.RoomRequestDTO;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.method.P;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = true)
public class RoomServiceTest {

    @Autowired EntityManager em;
    @Autowired RoomRepository roomRepository;
    @Autowired RoomMemberRepository roomMemberRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired RoomService roomService;

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
    @Transactional
    @Rollback(value = false)
    public void singleRoom_test() throws Exception {
        //given
        Member member = new Member("user@naver.com", "12345", "nick", 1, 4, Authority.ROLE_USER);

        em.persist(member);

        //when
        Long roomNumber = roomService.singleRoom(member.getNumber(), "기상", period, Category.ROUTINE, 2);

        //then
        Optional<Room> findSingleRoom = roomRepository.findById(roomNumber);
        assertThat(findSingleRoom.get().getNumber()).isEqualTo(roomNumber);

        List<Room> singleRooms = member.getSingleRooms();
        System.out.println("singleRooms = " + singleRooms);
    }

    @DisplayName("그룹 챌린지 생성 메서드 전체")
    @Test
    @Transactional
    @Rollback(value = false)
    public void groupRoomTest() throws Exception {
        //given
        Member member1 = new Member("user1@naver.com", "12345", "nick1", 1, 4, Authority.ROLE_USER);
        Member member2 = new Member("user2@naver.com", "11111", "nick2", 2, 4, Authority.ROLE_USER);
        Member member3 = new Member("user3@naver.com", "22222", "nick3", 3, 4, Authority.ROLE_USER);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        Long groupRoomNo = roomService.groupRoom(member1.getNumber(), "내일까지 마무으리", period, Category.MINDFULNESS, 0, member2, member3);

        //then
        RoomMember findRoomByMember = roomMemberRepository.findByMemberNumber(member1.getNumber());
        System.out.println("findRoomByMember" + findRoomByMember);

        RoomMember findRoom = roomMemberRepository.findByMemberNumber(member1.getNumber());
        assertThat(groupRoomNo).isEqualTo(findRoom.getRoom().getNumber());

        List<RoomMember> roomMemberList = member2.getRoomMemberList();
        for (RoomMember roomMember : roomMemberList) {
            System.out.println("roomMember = " + roomMember);
        }
    }

    @DisplayName("챌린지 삭제")
    @Test
    public void 챌린지_삭제() throws Exception {
        //given
        Room room = new Room("기상", period, Category.ROUTINE, RoomType.GROUP, 2);

        em.persist(room);

        //when
        roomRepository.deleteById(room.getNumber());

        //then
        long count = roomRepository.count();
        assertThat(count).isEqualTo(0);
    }

    Period period = new Period(30L);

}