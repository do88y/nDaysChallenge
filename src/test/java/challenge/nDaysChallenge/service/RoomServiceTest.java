//package challenge.nDaysChallenge.service;
//
//import challenge.nDaysChallenge.domain.Authority;
//import challenge.nDaysChallenge.domain.Member;
//import challenge.nDaysChallenge.domain.RoomMember;
//import challenge.nDaysChallenge.domain.room.*;
//import challenge.nDaysChallenge.repository.MemberRepository;
//import challenge.nDaysChallenge.repository.RoomMemberRepository;
//import challenge.nDaysChallenge.repository.room.RoomRepository;
//import org.junit.Test;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.WebApplicationContext;
//
//import javax.persistence.EntityManager;
//
//import java.security.Principal;
//import java.util.List;
//import java.util.Optional;
//
//import static org.assertj.core.api.Assertions.*;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//@Rollback(value = true)
//@MockBean(JpaMetamodelMappingContext.class)
//public class RoomServiceTest {
//
//    private MockMvc mvc;
//    private Principal mockPrincipal;
//    @Autowired WebApplicationContext context;
//
//    private UserDetailsImpl mockUserSetup() {
//        String id = "user@gmail.com";
//        String nickname = "테스트";
//        String pw = "test123";
//        int img = 1;
//        int roomLimit = 4;
//        Member testMember = new Member(id, pw, nickname, img, roomLimit, Authority.ROLE_USER);
//        UserDetailsImpl testUser = new UserDetailsImpl(testMember);
//        mockPrincipal = new UsernamePasswordAuthenticationToken(testUser, "", null);
//
//        return testUser;
//    }
//
//    @BeforeEach
//    public void setup() {
//        mvc = MockMvcBuilders.webAppContextSetup(context)
//                .apply(springSecurity(new MockSpringSecurityFilter()))
//                .build();
//    }
//
//    @Autowired EntityManager em;
//    @Autowired RoomRepository roomRepository;
//    @Autowired RoomMemberRepository roomMemberRepository;
//    @Autowired MemberRepository memberRepository;
//    @Autowired RoomService roomService;
//
//    @DisplayName("개인 챌린지 단독")
//    @Test
//    public void 개인_챌린지_생성() throws Exception {
//        //given
//        Room room = SingleRoom.builder()
//                .name("기상")
//                .period(new Period(30L))
//                .category(Category.ROUTINE)
//                .type(RoomType.SINGLE)
//                .passCount(2)
//                .build();
//
//        em.persist(room);
//
//        //when
//        Optional<Room> findRoom = roomRepository.findById(room.getNumber());
//
//
//        //then
//        assertThat(room.getNumber()).isEqualTo(findRoom.get().getNumber());
//    }
//
//
//    @DisplayName("개인 챌린지 생성 메서드 전체")
//    @Test
//    @Transactional
//    @Rollback(value = false)
//    public void singleRoom_test() throws Exception {
//        //give
//        UserDetailsImpl testUser = this.mockUserSetup();
//        Member member = new Member("user@naver.com", "12345", "nick", 1, 4, Authority.ROLE_USER);
//
//        em.persist(member);
//
//        //when
//        Room room = roomService.singleRoom(testUser, "기상", period, Category.ROUTINE, 2);
//
//        //then
//        Optional<Room> findSingleRoom = roomRepository.findById(room.getNumber());
//        assertThat(findSingleRoom.get()).isEqualTo(room);
//
//        List<Room> singleRooms = member.getSingleRooms();
//        for (Room singleRoom : singleRooms) {
//            assertThat(singleRoom).isEqualTo(room);
//        }
//    }
//
//    @DisplayName("그룹 챌린지 생성 메서드 전체")
//    @Test
//    @WithMockUser
//    @Transactional
//    @Rollback(value = false)
//    public void groupRoomTest() throws Exception {
//        //given
//        UserDetailsImpl testUser = this.mockUserSetup();
//        Member member1 = new Member("user1@naver.com", "12345", "nick1", 1, 4, Authority.ROLE_USER);
//        Member member2 = new Member("user2@naver.com", "11111", "nick2", 2, 4, Authority.ROLE_USER);
//        Member member3 = new Member("user3@naver.com", "22222", "nick3", 3, 4, Authority.ROLE_USER);
//        memberRepository.save(member1);
//        memberRepository.save(member2);
//        memberRepository.save(member3);
//
//        //when
//        Room groupRoomNo = roomService.groupRoom(testUser, "내일까지 마무으리", period, Category.MINDFULNESS, 0, member2, member3);
//
//        //then
//        RoomMember findRoomByMember = roomMemberRepository.findByMemberNumber(member1.getNumber());
//        System.out.println("findRoomByMember" + findRoomByMember);
//
//        RoomMember findRoom = roomMemberRepository.findByMemberNumber(member2.getNumber());
//        assertThat(groupRoomNo).isEqualTo(findRoom.getRoom());
//
//        List<RoomMember> roomMemberList = member2.getRoomMemberList();
//        for (RoomMember roomMember : roomMemberList) {
//            System.out.println("roomMember = " + roomMember);
//        }
//    }
//
//    @DisplayName("챌린지 삭제")
//    @Test
//    public void 챌린지_삭제() throws Exception {
//        //given
//        Room room = new Room("기상", period, Category.ROUTINE, RoomType.GROUP, 2);
//
//        em.persist(room);
//
//        //when
//        roomRepository.deleteById(room.getNumber());
//
//        //then
//        long count = roomRepository.count();
//        assertThat(count).isEqualTo(0);
//    }
//
//    Period period = new Period(30L);
//
//}