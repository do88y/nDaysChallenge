package challenge.nDaysChallenge.Room;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.room.GroupRoomRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.SingleRoomRepository;
import challenge.nDaysChallenge.service.RoomService;
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

import java.time.LocalDate;
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
    @Autowired SingleRoomRepository singleRoomRepository;
    @Autowired GroupRoomRepository groupRoomRepository;


    @Autowired RoomMemberRepository roomMemberRepository;
    @Autowired MemberRepository memberRepository;
    @Autowired RoomService roomService;

    @Test
    public void 챌린지_갯수() throws Exception {
        //given

        //when

        //then
    }

    @Test
    public void 개인_챌린지_builder() throws Exception {
        //given
        SingleRoom room = new SingleRoom("기상", period, Category.ROUTINE,2, "");


        em.persist(room);

        //when
        Room findRoom = roomRepository.findById(room.getNumber()).orElseThrow(() ->
                new IllegalArgumentException("해당 챌린지가 존재하지 않습니다."));


        //then
        assertThat(room.getNumber()).isEqualTo(findRoom.getNumber());
        assertThat(room.getReward()).isEqualTo(findRoom.getReward());
        assertThat(room.getPeriod()).isEqualTo(findRoom.getPeriod());
    }


    @Test
    @Transactional
    @Rollback(value = true)
    public void 개인_챌린지_생성_메서드_전체() throws Exception {
        //give
        Member member = new Member("user@naver.com", "12345", "nick0", 1, 4, Authority.ROLE_USER);

        em.persist(member);

        //when
        SingleRoom room = roomService.singleRoom(member, "기상", period, Category.ROUTINE, 2, "");
        em.flush();
        em.clear();

        //then
        SingleRoom findSingleRoom = singleRoomRepository.findById(room.getNumber()).orElseThrow(() ->
                new IllegalArgumentException("해당 챌린지가 존재하지 않습니다."));
        assertThat(findSingleRoom.getNumber()).isEqualTo(room.getNumber());


        //멤버에서 singleRooms 조회
        List<SingleRoom> singleRooms = member.getSingleRooms();
        for (Room singleRoom : singleRooms) {
            assertThat(singleRoom.getName()).isEqualTo(room.getName());
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
        GroupRoom groupRoom = roomService.groupRoom(member1, "내일까지 마무으리", period, Category.MINDFULNESS, 0,"", selectedMembers);
        em.flush();
        em.clear();

        //then
        RoomMember findRoomByMember = roomMemberRepository.findByMemberAndRoom(member1, groupRoom);
        System.out.println("findRoomByMember = " + findRoomByMember.getMember().getNickname());
        assertThat(groupRoom.getNumber()).isEqualTo(findRoomByMember.getRoom().getNumber());

        //멤버에서 roomMemberList 조회
        List<RoomMember> roomMemberList = member2.getRoomMemberList();
        for (RoomMember roomMember : roomMemberList) {
            System.out.println("roomMember = " + roomMember.getRoom().getName());
        }
    }

    @Test
    public void 챌린지_삭제() throws Exception {
        //given
        SingleRoom room = new SingleRoom("기상", period, Category.ROUTINE, 2, "");

        em.persist(room);
        em.flush();
        em.clear();

        //when
        roomRepository.deleteById(room.getNumber());

        //then
        long count = roomRepository.count();
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void 진행_챌린지_조회() throws Exception {
        //given
        SingleRoom singleRoom1 = SingleRoom.createRoom("기상", period, Category.ROUTINE, RoomType.SINGLE, RoomStatus.END, 2, "");
        SingleRoom singleRoom2 = SingleRoom.createRoom("공부", period, Category.ETC, RoomType.SINGLE, RoomStatus.END, 0, "");
        SingleRoom singleRoom3 = SingleRoom.createRoom("청소", period, Category.EXERCISE, RoomType.SINGLE, RoomStatus.CONTINUE, 10, "꿀잠");
        GroupRoom groupRoom = GroupRoom.createRoom("명상", period, Category.MINDFULNESS, RoomType.GROUP, RoomStatus.END, 20, "여행");
        singleRoomRepository.save(singleRoom1);
        singleRoomRepository.save(singleRoom2);
        singleRoomRepository.save(singleRoom3);
        groupRoomRepository.save(groupRoom);

        Member member = new Member("user@naver.com", "12345", "nick", 1, 4, Authority.ROLE_USER);
        memberRepository.save(member);

        SingleRoom createSingleRoom1 = singleRoom1.addRoom(singleRoom1, member);
        SingleRoom createSingleRoom2 = singleRoom2.addRoom(singleRoom2, member);
        SingleRoom createSingleRoom3 = singleRoom3.addRoom(singleRoom3, member);
        singleRoomRepository.save(createSingleRoom1);
        singleRoomRepository.save(createSingleRoom2);
        singleRoomRepository.save(createSingleRoom3);

        RoomMember roomMember = RoomMember.createRoomMember(member, groupRoom);
        roomMemberRepository.save(roomMember);

        em.flush();
        em.clear();

        //when
        List<SingleRoom> singleRooms = roomService.findSingleRooms(member);
        List<GroupRoom> groupRooms = roomService.findGroupRooms(member);

        //then
        assertThat(singleRooms.size()).isEqualTo(singleRoomRepository.count());
        assertThat(groupRooms.size()).isEqualTo(groupRoomRepository.count());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void 완료_챌린지_리스트() throws Exception {
        //given
        SingleRoom singleRoom1 = SingleRoom.createRoom("기상", period, Category.ROUTINE, RoomType.SINGLE, RoomStatus.END, 2, "");
        SingleRoom singleRoom2 = SingleRoom.createRoom("공부", period, Category.ETC, RoomType.SINGLE, RoomStatus.END, 0, "");
        SingleRoom singleRoom3 = SingleRoom.createRoom("청소", period, Category.EXERCISE, RoomType.SINGLE, RoomStatus.CONTINUE, 10, "꿀잠");
        GroupRoom groupRoom = GroupRoom.createRoom("명상", period, Category.MINDFULNESS, RoomType.GROUP, RoomStatus.END, 20, "여행");
        singleRoomRepository.save(singleRoom1);
        singleRoomRepository.save(singleRoom2);
        singleRoomRepository.save(singleRoom3);
        groupRoomRepository.save(groupRoom);

        Member member = new Member("user@naver.com", "12345", "nick", 1, 4, Authority.ROLE_USER);
        memberRepository.save(member);

        SingleRoom createSingleRoom1 = singleRoom1.addRoom(singleRoom1, member);
        SingleRoom createSingleRoom2 = singleRoom2.addRoom(singleRoom2, member);
        SingleRoom createSingleRoom3 = singleRoom3.addRoom(singleRoom3, member);
        singleRoomRepository.save(createSingleRoom1);
        singleRoomRepository.save(createSingleRoom2);
        singleRoomRepository.save(createSingleRoom3);

        RoomMember roomMember = RoomMember.createRoomMember(member, groupRoom);
        roomMemberRepository.save(roomMember);

        em.flush();
        em.clear();

        //when
        List<Room> finishedRooms = roomService.findFinishedRooms(member);

        //then
        assertThat(finishedRooms.size()).isEqualTo(3);
    }

    Period period = new Period(LocalDate.now(),30L);

}
