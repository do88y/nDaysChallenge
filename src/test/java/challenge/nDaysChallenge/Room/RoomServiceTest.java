package challenge.nDaysChallenge.Room;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.StampRepository;
import challenge.nDaysChallenge.repository.room.GroupRoomRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.RoomSearch;
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
import java.util.*;

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
    @Autowired StampRepository stampRepository;


    @Test
    public void 개인_챌린지_builder() throws Exception {
        //given
        SingleRoom room = new SingleRoom("기상", period, Category.ROUTINE,2, "", 0, 0);

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
    @Rollback(value = true)
    public void 개인_챌린지_생성_메서드_전체() throws Exception {
        //give
        Member member = new Member("user@naver.com", "12345", "nick0", 1, 4, Authority.ROLE_USER);

        em.persist(member);

        //when
        SingleRoom room = roomService.singleRoom(member, "기상", period, Category.ROUTINE, 2, "", 0, 0);
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
    @Rollback(value = true)
    public void 그룹_챌린지_생성_메서드_전체() throws Exception {
        //given
        Set<Long> selectedMembers = new HashSet<>();
        Member member1 = new Member("user1@naver.com", "12345", "nick1", 1, 4, Authority.ROLE_USER);
        Member member2 = new Member("user2@naver.com", "11111", "nick2", 2, 4, Authority.ROLE_USER);
        Member member3 = new Member("user3@naver.com", "22222", "nick3", 3, 4, Authority.ROLE_USER);
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        selectedMembers.add(member2.getNumber());
        selectedMembers.add(member3.getNumber());

        //when
        GroupRoom groupRoom = roomService.groupRoom(member1, "내일까지 마무으리", period, Category.MINDFULNESS, 0,"", 0, 0, selectedMembers);
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

    @Test(expected = NoSuchElementException.class)
    @Rollback(value = true)
    public void 챌린지_삭제() throws Exception {
        //given
        Member member = new Member("user@naver.com", "12345", "nick0", 1, 4, Authority.ROLE_USER);
        em.persist(member);

        SingleRoom room = roomService.singleRoom(member, "기상", period, Category.ROUTINE, 2, "", 0, 0);

        em.flush();
        em.clear();

        //when
        Long roomNumber = room.getNumber();
        roomService.deleteRoom(member, room.getNumber());

        //then
        assertThat(roomRepository.findById(roomNumber).get());
    }

    @Test
    @Transactional
    @Rollback(value = true)
    public void 진행_챌린지_조회() throws Exception {
        //given
        SingleRoom singleRoom1 = new SingleRoom("기상", period, Category.ROUTINE, 2, "", 0, 0);
        SingleRoom singleRoom2 = new SingleRoom("공부", period, Category.ETC, 0, "", 0, 0);
        SingleRoom singleRoom3 = new SingleRoom("청소", period, Category.EXERCISE, 10, "꿀잠", 0, 0);
        GroupRoom groupRoom = new GroupRoom("명상", period, Category.MINDFULNESS, 20, "여행", 0, 0);
        singleRoomRepository.save(singleRoom1);
        singleRoomRepository.save(singleRoom2);
        singleRoomRepository.save(singleRoom3);
        groupRoomRepository.save(groupRoom);

        Stamp stamp1 = Stamp.createStamp(singleRoom1);
        Stamp stamp2 = Stamp.createStamp(singleRoom2);
        Stamp stamp3 = Stamp.createStamp(singleRoom3);
        Stamp stamp4 = Stamp.createStamp(groupRoom);

        Member member = new Member("user@naver.com", "12345", "nick", 1, 4, Authority.ROLE_USER);
        memberRepository.save(member);

        SingleRoom createSingleRoom1 = singleRoom1.addRoom(singleRoom1, member, stamp1);
        SingleRoom createSingleRoom2 = singleRoom2.addRoom(singleRoom2, member, stamp2);
        SingleRoom createSingleRoom3 = singleRoom3.addRoom(singleRoom3, member, stamp3);
        singleRoomRepository.save(createSingleRoom1);
        singleRoomRepository.save(createSingleRoom2);
        singleRoomRepository.save(createSingleRoom3);

        RoomMember roomMember = RoomMember.createRoomMember(member, groupRoom, Stamp.createStamp(groupRoom));
        roomMemberRepository.save(roomMember);

        em.flush();
        em.clear();

        //when
        roomService.changeStatus(singleRoom1.getNumber());

        List<SingleRoom> singleRooms = roomService.findSingleRooms(member);
        List<GroupRoom> groupRooms = roomService.findGroupRooms(member);

        //then
        assertThat(singleRooms.size()).isEqualTo(2);
        assertThat(groupRooms.size()).isEqualTo(1);
        for (SingleRoom singleRoom : singleRooms) {
            System.out.println("singleRoom = " + singleRoom.getName());
        }
    }

    @Test
    @Rollback(value = true)
    public void 완료_개인챌린지_리스트() throws Exception {
        //given
        SingleRoom singleRoom1 = new SingleRoom("기상", period, Category.ROUTINE, 2, "", 0, 0);
        SingleRoom singleRoom2 = new SingleRoom("공부", period, Category.ETC, 0, "", 0, 0);
        SingleRoom singleRoom3 = new SingleRoom("청소", period, Category.EXERCISE, 10, "꿀잠", 0, 0);
        singleRoomRepository.save(singleRoom1);
        singleRoomRepository.save(singleRoom2);
        singleRoomRepository.save(singleRoom3);

        Stamp stamp1 = Stamp.createStamp(singleRoom1);
        Stamp stamp2 = Stamp.createStamp(singleRoom2);
        Stamp stamp3 = Stamp.createStamp(singleRoom3);

        Member member = new Member("user@naver.com", "12345", "nick", 1, 4, Authority.ROLE_USER);
        memberRepository.save(member);

        SingleRoom createSingleRoom1 = singleRoom1.addRoom(singleRoom1, member, stamp1);
        SingleRoom createSingleRoom2 = singleRoom2.addRoom(singleRoom2, member, stamp2);
        SingleRoom createSingleRoom3 = singleRoom3.addRoom(singleRoom3, member, stamp3);
        singleRoomRepository.save(createSingleRoom1);
        singleRoomRepository.save(createSingleRoom2);
        singleRoomRepository.save(createSingleRoom3);

        em.flush();
        em.clear();

        //when
        roomService.changeStatus(singleRoom1.getNumber());
        roomService.changeStatus(singleRoom2.getNumber());
        List<SingleRoom> finishedRooms = roomService.findFinishedSingleRooms(member);

        //then
        assertThat(finishedRooms.size()).isEqualTo(2);
    }

    @Test
    @Rollback(value = true)
    public void 완료_그룹챌린지_리스트() throws Exception {
        //given
        GroupRoom groupRoom = new GroupRoom("명상", period, Category.MINDFULNESS, 20, "여행", 0, 0);
        groupRoomRepository.save(groupRoom);

        Member member = new Member("user@naver.com", "12345", "nick", 1, 4, Authority.ROLE_USER);
        memberRepository.save(member);

        RoomMember roomMember = RoomMember.createRoomMember(member, groupRoom, Stamp.createStamp(groupRoom));
        roomMemberRepository.save(roomMember);

        em.flush();
        em.clear();

        //when
        roomService.changeStatus(groupRoom.getNumber());
        List<GroupRoom> finishedRooms = roomService.findFinishedGroupRooms(member);

        //then
        assertThat(finishedRooms.size()).isEqualTo(1);
    }

    @Test
    public void update_stamp_쿼리() throws Exception {
        //given
        SingleRoom singleRoom = new SingleRoom("기상", period, Category.ROUTINE, 2, "", 0, 0);
        singleRoomRepository.save(singleRoom);
        Stamp stamp = Stamp.createStamp(singleRoom);
        stampRepository.save(stamp);

        //when
        Stamp updateStamp = stamp.updateStamp(singleRoom, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        em.flush();
        em.clear();

        //then
        assertThat(updateStamp.getDay1()).isEqualTo(1);
    }

    @Test
    public void 관리자_SingleRoom_status_select_쿼리() throws Exception {
        //given
        RoomSearch roomSearch = new RoomSearch("END", null);
        List<Room> singleRoomAdmin = roomRepository.findSingleRoomAdmin(roomSearch);
        System.out.println("singleRoomAdmin.size() = " + singleRoomAdmin.size());
    }
    @Test
    public void 관리자_SingleRoom_member_select_쿼리() throws Exception {
        //given
        RoomSearch roomSearch = new RoomSearch(null, "aaaa@naver.com");
        List<Room> singleRoomAdmin = roomRepository.findSingleRoomAdmin(roomSearch);
    }

    Period period = new Period(LocalDate.now(),30L);

}
