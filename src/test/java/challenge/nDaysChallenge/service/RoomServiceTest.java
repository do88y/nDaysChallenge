package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.security.MockSpringSecurityFilter;
import challenge.nDaysChallenge.security.UserDetailsImpl;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Rollback(value = true)
@MockBean(JpaMetamodelMappingContext.class)
public class RoomServiceTest {

    private MockMvc mvc;
    private Principal mockPrincipal;
    @Autowired WebApplicationContext context;

    private UserDetailsImpl mockUserSetup() {
        String id = "user@gmail.com";
        String nickname = "테스트";
        String pw = "test123";
        int img = 1;
        int roomLimit = 4;
        Member testMember = new Member(id, pw, nickname, img, roomLimit, Authority.ROLE_USER);
        UserDetailsImpl testUser = new UserDetailsImpl(testMember);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUser, "", null);

        return testUser;
    }

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    @Autowired EntityManager em;
    @Autowired RoomRepository roomRepository;
    @Autowired RoomMemberRepository roomMemberRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired RoomService roomService;

    @Test
    @DisplayName("개인 챌린지 단독")
    public void singleRoom() throws Exception {
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
        Optional<Room> findRoom = roomRepository.findById(room.getNumber());


        //then
        assertThat(room.getNumber()).isEqualTo(findRoom.get().getNumber());
    }

    @Test
    @DisplayName("개인 챌린지 생성 메서드 전체")
    @Transactional
    @Rollback(value = true)
    public void singleRoomTest() throws Exception {
        //give
        UserDetailsImpl testUser = this.mockUserSetup();
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
    @DisplayName("그룹 챌린지 생성 메서드 전체")
    @WithMockUser
    @Transactional
    @Rollback(value = true)
    public void groupRoomTest() throws Exception {
        //given
        UserDetailsImpl testUser = this.mockUserSetup();
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
    @DisplayName("챌린지 삭제")
    public void deleteRoom() throws Exception {
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