package challenge.nDaysChallenge.Room;

import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.RoomMember;
import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.StampDto;
import challenge.nDaysChallenge.dto.response.Room.RoomResponseDto;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.StampRepository;
import challenge.nDaysChallenge.repository.room.GroupRoomRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.RoomSearch;
import challenge.nDaysChallenge.repository.room.SingleRoomRepository;
import challenge.nDaysChallenge.service.RoomService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
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
        SingleRoom room = new SingleRoom("기상", this.period, Category.ROUTINE, 2, "", 0, 0);
        em.persist(room);

        //when
        Room findRoom = roomRepository.findByNumber(room.getNumber()).orElseThrow(() ->
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
        Member member = new Member("user@naver.com", "12345", "nick0", 1, Authority.ROLE_USER);
        em.persist(member);

        //when
        RoomResponseDto room = roomService.singleRoom(member, "기상", period, Category.ROUTINE, 2, "", 0, 0);
        em.flush();
        em.clear();

        //then
        SingleRoom findSingleRoom = singleRoomRepository.findById(room.getRoomNumber()).orElseThrow(() ->
                new IllegalArgumentException("해당 챌린지가 존재하지 않습니다."));
        assertThat(findSingleRoom.getNumber()).isEqualTo(room.getRoomNumber());


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
        Member member1 = new Member("user1@naver.com", "12345", "nick1", 1, Authority.ROLE_USER);
        Member member2 = new Member("user2@naver.com", "11111", "nick2", 2, Authority.ROLE_USER);
        Member member3 = new Member("user3@naver.com", "22222", "nick3", 3, Authority.ROLE_USER);
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
        Member member = new Member("user@naver.com", "12345", "nick0", 1, Authority.ROLE_USER);
        em.persist(member);

        RoomResponseDto room = roomService.singleRoom(member, "기상", period, Category.ROUTINE, 2, "", 0, 0);

        em.flush();
        em.clear();

        //when
        Long roomNumber = room.getRoomNumber();
        roomService.deleteRoom(member, room.getRoomNumber());

        //then
        assertThat(roomRepository.findByNumber(roomNumber).get());
    }

    @Test
    @Rollback(value = true)
    public void 진행_챌린지_조회() throws Exception {
        //given
        Member member = new Member("user@naver.com", "12345", "nick0", 1, Authority.ROLE_USER);
        memberRepository.save(member);

        SingleRoom singleRoom1 = new SingleRoom("기상", this.period, Category.ROUTINE, 2, "", 0, 0);
        SingleRoom singleRoom2 = new SingleRoom("공부", this.period, Category.ETC, 0, "", 0, 0);
        SingleRoom singleRoom3 = new SingleRoom("청소", this.period, Category.EXERCISE, 10, "꿀잠", 0, 0);
        singleRoomRepository.save(singleRoom1);
        singleRoomRepository.save(singleRoom2);
        singleRoomRepository.save(singleRoom3);

        Stamp stamp1 = Stamp.createStamp(singleRoom1, member);
        Stamp stamp2 = Stamp.createStamp(singleRoom2, member);
        Stamp stamp3 = Stamp.createStamp(singleRoom3, member);
        stampRepository.save(stamp1);
        stampRepository.save(stamp2);
        stampRepository.save(stamp3);

        singleRoom1.addRoom(singleRoom1, member, stamp1);
        singleRoom2.addRoom(singleRoom2, member, stamp2);
        singleRoom3.addRoom(singleRoom3, member, stamp3);

        GroupRoom groupRoom = new GroupRoom(member, "명상", this.period, Category.MINDFULNESS, 20, "여행", 0, 0);
        groupRoomRepository.save(groupRoom);
        Stamp stamp4 = Stamp.createStamp(singleRoom1, member);
        stampRepository.save(stamp4);
        RoomMember roomMember = RoomMember.createRoomMember(member, groupRoom, stamp4);
        roomMemberRepository.save(roomMember);

        //when
        roomService.changeStatus(singleRoom1.getNumber());

        em.flush();
        em.clear();

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
        Member member = new Member("user@naver.com", "12345", "nick0", 1, Authority.ROLE_USER);
        memberRepository.save(member);

        SingleRoom singleRoom1 = new SingleRoom("기상", this.period, Category.ROUTINE, 2, "", 0, 0);
        SingleRoom singleRoom2 = new SingleRoom("공부", this.period, Category.ETC, 0, "", 0, 0);
        SingleRoom singleRoom3 = new SingleRoom("청소", this.period, Category.EXERCISE, 10, "꿀잠", 0, 0);
        singleRoomRepository.save(singleRoom1);
        singleRoomRepository.save(singleRoom2);
        singleRoomRepository.save(singleRoom3);

        Stamp stamp1 = Stamp.createStamp(singleRoom1, member);
        Stamp stamp2 = Stamp.createStamp(singleRoom2, member);
        Stamp stamp3 = Stamp.createStamp(singleRoom3, member);
        stampRepository.save(stamp1);
        stampRepository.save(stamp2);
        stampRepository.save(stamp3);

        singleRoom1.addRoom(singleRoom1, member, stamp1);
        singleRoom2.addRoom(singleRoom2, member, stamp2);
        singleRoom3.addRoom(singleRoom3, member, stamp3);

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
        Member member = new Member("user@naver.com", "12345", "nick0", 1, Authority.ROLE_USER);
        memberRepository.save(member);

        GroupRoom groupRoom = new GroupRoom(member, "명상", this.period, Category.MINDFULNESS, 20, "여행", 0, 0);
        groupRoomRepository.save(groupRoom);

        RoomMember roomMember = RoomMember.createRoomMember(member, groupRoom, Stamp.createStamp(groupRoom, member));
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
        Member member = new Member("user@naver.com", "12345", "nick0", 1, Authority.ROLE_USER);
        memberRepository.save(member);

        SingleRoom room = new SingleRoom("기상", this.period, Category.ROUTINE, 2, "", 0, 0);
        singleRoomRepository.save(room);
        Stamp stamp1 = Stamp.createStamp(room, member);
        stampRepository.save(stamp1);

        //when
        Stamp updateStamp = stamp1.updateStamp(room, "o");
        em.flush();
        em.clear();
        stamp1.updateStamp(room, "x");
        em.flush();
        em.clear();

        //then
        System.out.println("updateStamp.getDay() = " + updateStamp.getDay());
        assertThat(updateStamp.getDay()).isEqualTo("ox");
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

    @Test
    public void findByRoomAndMember_쿼리() throws Exception {
        //given
        SingleRoom room = new SingleRoom("기상", this.period, Category.ROUTINE, 2, "", 0, 0);
        Member member = new Member("user@naver.com", "12345", "nick0", 1, Authority.ROLE_USER);

        //when
        roomRepository.save(room);
        memberRepository.save(member);

        //then
        stampRepository.findByRoomAndMember(room, member);
    }

    @Test
    public void 스탬프_카운트_업데이트 () throws Exception {
        //given
        Member member = new Member("user@naver.com", "12345", "nick0", 1, Authority.ROLE_USER);
        memberRepository.save(member);
        RoomResponseDto roomDto = roomService.singleRoom(member, "기상", period, Category.ROUTINE, 2, "", 0, 0);
        Room room = roomRepository.findByNumber(roomDto.getRoomNumber()).orElseThrow(() -> new RuntimeException("해당 챌린지가 없습니다."));
        //when
        roomService.updateStamp(member, room.getNumber(), new StampDto(room.getNumber(), room.getStamp().getNumber(), "o", room.getStamp().getSuccessCount(), room.getPassCount()));
        roomService.updateStamp(member, room.getNumber(), new StampDto(room.getNumber(), room.getStamp().getNumber(), "o", room.getStamp().getSuccessCount(), room.getPassCount()));
        roomService.updateStamp(member, room.getNumber(), new StampDto(room.getNumber(), room.getStamp().getNumber(), "x", room.getStamp().getSuccessCount(), room.getPassCount()));

        em.flush();
        em.clear();
        //then
        assertThat(room.getStamp().getSuccessCount()).isEqualTo(2);
        assertThat(room.getStamp().getDay()).isEqualTo("oox");
    }

    //기간
    Period period = new Period(LocalDate.now(),30L);

}
