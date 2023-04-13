package challenge.nDaysChallenge.Room;

import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.RoomMember;
import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.StampDto;
import challenge.nDaysChallenge.dto.response.room.RoomResponseDto;
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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
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
        SingleRoom room = new SingleRoom("기상", this.period, Category.ROUTINE, 2, "");
        em.persist(room);
        em.flush();
        em.clear();

        //when
        Room findRoom = roomRepository.findByNumber(room.getNumber()).orElseThrow(() ->
                new IllegalArgumentException("해당 챌린지가 존재하지 않습니다."));

        //then
        assertThat(room.getNumber()).isEqualTo(findRoom.getNumber());
        assertThat(room.getReward()).isEqualTo(findRoom.getReward());
        assertThat(room.getPeriod().getTotalDays()).isEqualTo(findRoom.getPeriod().getTotalDays());
    }

    @Test
    public void roomMember_연관관계_테스트() throws Exception {
        //given
        Set<Long> selectedMembers = new HashSet<>();
        Member member1 = Member.builder()
                .id("use1r@naver.com")
                .pw("12345")
                .nickname("abc1")
                .authority(Authority.ROLE_USER)
                .build();
        Member member2 = Member.builder()
                .id("user2@naver.com")
                .pw("11111")
                .nickname("abc2")
                .authority(Authority.ROLE_USER)
                .build();
        Member member3 = Member.builder()
                .id("user3@naver.com")
                .pw("22222")
                .nickname("abc3")
                .authority(Authority.ROLE_USER)
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        selectedMembers.add(member2.getNumber());
        selectedMembers.add(member3.getNumber());

        //when
        GroupRoom groupRoom = roomService.groupRoom(member1, "내일까지 마무으리", period, Category.MINDFULNESS, 0,"", selectedMembers);
        em.flush();
        em.clear();

        //then
        assertThat(groupRoom.getRoomMemberList().size()).isEqualTo(3);
        assertThat(groupRoom.getRoomMemberList().get(0)).isInstanceOf(RoomMember.class);
    }

    @Test
    public void 개인_챌린지_생성_메서드_전체() throws Exception {
        //give
        Member member = Member.builder()
                .id("user@naver.com")
                .pw("12345")
                .nickname("abc")
                .authority(Authority.ROLE_USER)
                .build();
        em.persist(member);

        //when
        RoomResponseDto room = roomService.singleRoom(member, "기상", period, Category.ROUTINE, 2, "");
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
    public void 그룹_챌린지_생성_메서드_전체() throws Exception {
        //given
        Set<Long> selectedMembers = new HashSet<>();
        Member member1 = Member.builder()
                .id("use1r@naver.com")
                .pw("12345")
                .nickname("abc")
                .authority(Authority.ROLE_USER)
                .build();
        Member member2 = Member.builder()
                .id("user2@naver.com")
                .pw("11111")
                .nickname("abc")
                .authority(Authority.ROLE_USER)
                .build();
        Member member3 = Member.builder()
                .id("user3@naver.com")
                .pw("22222")
                .nickname("abc")
                .authority(Authority.ROLE_USER)
                .build();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        selectedMembers.add(member2.getNumber());
        selectedMembers.add(member3.getNumber());

        //when
        GroupRoom groupRoom1 = roomService.groupRoom(member1, "내일까지 마무으리", period, Category.MINDFULNESS, 0,"", selectedMembers);
        em.flush();
        em.clear();

        //then
        RoomMember findRoomByMember = roomMemberRepository.findByMemberAndRoom(member1, groupRoom1);
        assertThat(groupRoom1.getNumber()).isEqualTo(findRoomByMember.getRoom().getNumber());

    }

    @Test(expected = NoSuchElementException.class)
    public void 챌린지_삭제() throws Exception {
        //given
        Member member = Member.builder()
                .id("use1r@naver.com")
                .pw("12345")
                .nickname("abc")
                .authority(Authority.ROLE_USER)
                .build();
        em.persist(member);

        RoomResponseDto room = roomService.singleRoom(member, "기상", period, Category.ROUTINE, 2, "");

        em.flush();
        em.clear();

        //when
        Long roomNumber = room.getRoomNumber();
        roomService.deleteRoom(member, room.getRoomNumber());

        //then
        assertThat(roomRepository.findByNumber(roomNumber).get());

        fail("exception should be accrued");
    }

    @Test
    public void 진행_챌린지_조회() throws Exception {
        //given
        Member member = Member.builder()
                .id("use1r@naver.com")
                .pw("12345")
                .nickname("abc")
                .authority(Authority.ROLE_USER)
                .build();
        memberRepository.save(member);

        SingleRoom singleRoom1 = new SingleRoom("기상", this.period, Category.ROUTINE, 2, "");
        SingleRoom singleRoom2 = new SingleRoom("공부", this.period, Category.ETC, 0, "");
        SingleRoom singleRoom3 = new SingleRoom("청소", this.period, Category.EXERCISE, 10, "꿀잠");
        singleRoomRepository.save(singleRoom1);
        singleRoomRepository.save(singleRoom2);
        singleRoomRepository.save(singleRoom3);

        Stamp stamp1 = Stamp.createStamp(singleRoom1, member);
        Stamp stamp2 = Stamp.createStamp(singleRoom2, member);
        Stamp stamp3 = Stamp.createStamp(singleRoom3, member);
        stampRepository.save(stamp1);
        stampRepository.save(stamp2);
        stampRepository.save(stamp3);

        singleRoom1.addRoom(member, stamp1);
        singleRoom2.addRoom(member, stamp2);
        singleRoom3.addRoom(member, stamp3);

        GroupRoom groupRoom = new GroupRoom(member, "명상", this.period, Category.MINDFULNESS, 20, "여행");
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
    public void 완료_개인챌린지_리스트() throws Exception {
        //given
        Member member = Member.builder()
                .id("use1r@naver.com")
                .pw("12345")
                .nickname("abc")
                .authority(Authority.ROLE_USER)
                .build();
        memberRepository.save(member);

        SingleRoom singleRoom1 = new SingleRoom("기상", this.period, Category.ROUTINE, 2, "");
        SingleRoom singleRoom2 = new SingleRoom("공부", this.period, Category.ETC, 0, "");
        SingleRoom singleRoom3 = new SingleRoom("청소", this.period, Category.EXERCISE, 10, "꿀잠");
        singleRoomRepository.save(singleRoom1);
        singleRoomRepository.save(singleRoom2);
        singleRoomRepository.save(singleRoom3);

        Stamp stamp1 = Stamp.createStamp(singleRoom1, member);
        Stamp stamp2 = Stamp.createStamp(singleRoom2, member);
        Stamp stamp3 = Stamp.createStamp(singleRoom3, member);
        stampRepository.save(stamp1);
        stampRepository.save(stamp2);
        stampRepository.save(stamp3);

        singleRoom1.addRoom(member, stamp1);
        singleRoom2.addRoom(member, stamp2);
        singleRoom3.addRoom(member, stamp3);

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
    public void 완료_그룹챌린지_리스트() throws Exception {
        //given
        Member member = Member.builder()
                .id("use1r@naver.com")
                .pw("12345")
                .nickname("abc")
                .authority(Authority.ROLE_USER)
                .build();
        memberRepository.save(member);

        GroupRoom groupRoom = new GroupRoom(member, "명상", this.period, Category.MINDFULNESS, 20, "여행");
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
        Member member = Member.builder()
                .id("use1r@naver.com")
                .pw("12345")
                .nickname("abc")
                .authority(Authority.ROLE_USER)
                .build();
        memberRepository.save(member);

        SingleRoom room = new SingleRoom("기상", this.period, Category.ROUTINE, 2, "");
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
    public void findByRoomAndMember_쿼리() throws Exception {
        //given
        SingleRoom room = new SingleRoom("기상", this.period, Category.ROUTINE, 2, "");
        Member member = Member.builder()
                .id("use1r@naver.com")
                .pw("12345")
                .nickname("abc")
                .authority(Authority.ROLE_USER)
                .build();
        //when
        roomRepository.save(room);
        memberRepository.save(member);

        //then
        stampRepository.findByRoomAndMember(room, member);
    }

    @Test
    public void 스탬프_카운트_업데이트 () throws Exception {
        //given
        Member member = Member.builder()
                .id("use1r@naver.com")
                .pw("12345")
                .nickname("abc")
                .authority(Authority.ROLE_USER)
                .build();
        memberRepository.save(member);
        RoomResponseDto roomDto = roomService.singleRoom(member, "기상", period, Category.ROUTINE, 2, "");
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