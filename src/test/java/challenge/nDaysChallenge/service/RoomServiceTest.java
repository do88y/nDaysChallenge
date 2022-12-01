package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = true)
@MockBean(JpaMetamodelMappingContext.class)
public class RoomServiceTest {

    @Autowired EntityManager em;
    @Autowired RoomRepository roomRepository;
    @Autowired RoomMemberRepository roomMemberRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired RoomService roomService;

    @Test
    public void 개인_챌린지_builder() throws Exception {
        //given
        Room room = SingleRoom.builder()
                .name("기상")
                .period(new Period(30L))
                .category(Category.ROUTINE)
                .type(RoomType.SINGLE)
                .passCount(2)
                .reward("유럽여행")
                .build();

        em.persist(room);

        //when
        Optional<Room> findRoom = roomRepository.findById(room.getNumber());


        //then
        assertThat(room.getNumber()).isEqualTo(findRoom.get().getNumber());
        assertThat(room.getReward()).isEqualTo(findRoom.get().getReward());
        assertThat(room.getPeriod()).isEqualTo(findRoom.get().getPeriod());
    }

    @Test
    public void createRoom_메서드_테스트() throws Exception {
        //given

        //when

        //then
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void 개인_챌린지_생성_메서드_전체() throws Exception {
        //give
        Member member = new Member("user@naver.com", "12345", "nick", 1, 4, Authority.ROLE_USER);

        em.persist(member);

        //when
        Room room = roomService.singleRoom(member, "기상", period, Category.ROUTINE, 2);

        //then
        Optional<Room> findSingleRoom = roomRepository.findById(room.getNumber());
        assertThat(findSingleRoom.get()).isEqualTo(room);

        //멤버에서 singleRooms 조회
        List<Room> singleRooms = member.getSingleRooms();
        for (Room singleRoom : singleRooms) {
            assertThat(singleRoom).isEqualTo(room);
            System.out.println("singleRoom = " + singleRoom.getName());
        }
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void 그룹_챌린지_생성_메서드_전체() throws Exception {
        //given
        Set<Member> selectedMembers = new HashSet<>();
        Member member1 = new Member("user1@naver.com", "12345", "nick1", 1, 4, Authority.ROLE_USER);
        Member member2 = new Member("user2@naver.com", "11111", "nick2", 2, 4, Authority.ROLE_USER);
        Member member3 = new Member("user3@naver.com", "22222", "nick3", 3, 4, Authority.ROLE_USER);
        selectedMembers.add(member2);
        selectedMembers.add(member3);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        //when
        Room groupRoom = roomService.groupRoom(member1, "내일까지 마무으리", period, Category.MINDFULNESS, 0, selectedMembers);

        //then
        RoomMember findRoomByMember = roomMemberRepository.findByMemberAndRoom(member1, groupRoom);
        System.out.println("findRoomByMember = " + findRoomByMember);
        assertThat(groupRoom).isEqualTo(findRoomByMember.getRoom());

        //멤버에서 roomMemberList 조회
        List<RoomMember> roomMemberList = member2.getRoomMemberList();
        for (RoomMember roomMember : roomMemberList) {
            System.out.println("roomMember = " + roomMember.getRoom().getName());
        }
    }

    @Test
    public void 챌린지_삭제() throws Exception {
        //given
        Room room = new Room("기상", period, Category.ROUTINE, RoomType.GROUP, 2, "");

        em.persist(room);

        //when
        roomRepository.deleteById(room.getNumber());

        //then
        long count = roomRepository.count();
        assertThat(count).isEqualTo(0);
    }

    Period period = new Period(30L);

}
