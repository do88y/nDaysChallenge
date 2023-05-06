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
        GroupRoom groupRoom = roomService.groupRoom(member1.getId(), "내일까지 마무으리", period, Category.MINDFULNESS, 0,"", selectedMembers);
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
        RoomResponseDto room1 = roomService.singleRoom(member.getId(), "기상", period, Category.ROUTINE, 2, "");
        roomService.singleRoom(member.getId(), "운동", period, Category.ROUTINE, 2, "");
        em.flush();
        em.clear();

        //then
        SingleRoom findSingleRoom = singleRoomRepository.findById(room1.getRoomNumber()).orElseThrow(() ->
                new IllegalArgumentException("해당 챌린지가 존재하지 않습니다."));
        assertThat(findSingleRoom.getNumber()).isEqualTo(room1.getRoomNumber());


        //멤버에서 singleRooms 조회
        List<SingleRoom> singleRooms = member.getSingleRooms();
        assertThat(singleRooms)
                .extracting("name")
                .containsExactly("기상", "운동");
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
        GroupRoom groupRoom1 = roomService.groupRoom(member1.getId(), "내일까지 마무으리", period, Category.MINDFULNESS, 0,"", selectedMembers);
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

        RoomResponseDto room = roomService.singleRoom(member.getId(), "기상", period, Category.ROUTINE, 2, "");

        em.flush();
        em.clear();

        //when
        Member findMember = roomRepository.findMemberByRoomNumber(room.getRoomNumber()).get();
        roomService.deleteRoom(findMember.getId(), room.getRoomNumber());

        //then
        assertThat(roomRepository.findByNumber(room.getRoomNumber()).get());

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

        Stamp stamp1 = Stamp.createStamp(singleRoom1);
        Stamp stamp2 = Stamp.createStamp(singleRoom2);
        Stamp stamp3 = Stamp.createStamp(singleRoom3);
        stampRepository.save(stamp1);
        stampRepository.save(stamp2);
        stampRepository.save(stamp3);

        singleRoom1.addRoom(member, stamp1);
        singleRoom2.addRoom(member, stamp2);
        singleRoom3.addRoom(member, stamp3);

        GroupRoom groupRoom = new GroupRoom(member, "명상", this.period, Category.MINDFULNESS, 20, "여행");
        groupRoomRepository.save(groupRoom);
        Stamp stamp4 = Stamp.createStamp(singleRoom1);
        stampRepository.save(stamp4);
        RoomMember roomMember = RoomMember.createRoomMember(member, groupRoom, stamp4);
        roomMemberRepository.save(roomMember);

        //when
        singleRoom1.end();

        em.flush();
        em.clear();

        List<SingleRoom> singleRooms = roomService.findSingleRooms(member.getId());
        List<GroupRoom> groupRooms = roomService.findGroupRooms(member.getId());

        //then
        assertThat(singleRooms.size()).isEqualTo(2);
        assertThat(groupRooms.size()).isEqualTo(1);
        assertThat(singleRooms)
                .extracting("name")
                .containsExactly("공부", "청소");
        assertThat(groupRooms)
                .extracting("name")
                .containsExactly("명상");
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

        Stamp stamp1 = Stamp.createStamp(singleRoom1);
        Stamp stamp2 = Stamp.createStamp(singleRoom2);
        Stamp stamp3 = Stamp.createStamp(singleRoom3);
        stampRepository.save(stamp1);
        stampRepository.save(stamp2);
        stampRepository.save(stamp3);

        singleRoom1.addRoom(member, stamp1);
        singleRoom2.addRoom(member, stamp2);
        singleRoom3.addRoom(member, stamp3);

        GroupRoom groupRoom1 = new GroupRoom(member, "그룹기상", this.period, Category.MINDFULNESS, 20, "여행");
        GroupRoom groupRoom2 = new GroupRoom(member, "그룹공부", this.period, Category.MINDFULNESS, 20, "여행");
        GroupRoom groupRoom3 = new GroupRoom(member, "그룹청소", this.period, Category.MINDFULNESS, 20, "여행");
        groupRoomRepository.save(groupRoom1);
        groupRoomRepository.save(groupRoom2);
        groupRoomRepository.save(groupRoom3);

        RoomMember roomMember1 = RoomMember.createRoomMember(member, groupRoom1, Stamp.createStamp(groupRoom1));
        RoomMember roomMember2 = RoomMember.createRoomMember(member, groupRoom2, Stamp.createStamp(groupRoom2));
        RoomMember roomMember3 = RoomMember.createRoomMember(member, groupRoom3, Stamp.createStamp(groupRoom3));
        roomMemberRepository.save(roomMember1);
        roomMemberRepository.save(roomMember2);
        roomMemberRepository.save(roomMember3);

        singleRoom1.end();
        singleRoom2.end();
        groupRoom1.end();
        groupRoom2.end();

        em.flush();
        em.clear();

        //when
        List<RoomResponseDto> finishedRooms = roomService.findFinishedRooms(member.getId());

        //then
        assertThat(finishedRooms.size()).isEqualTo(4);
        assertThat(finishedRooms)
                .extracting("name")
                .containsExactly("기상", "공부", "그룹기상", "그룹공부");
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
        Stamp stamp1 = Stamp.createStamp(room);
        stampRepository.save(stamp1);

        //when
        stamp1.updateStamp(room, "o");
        Stamp updateStamp = stamp1.updateStamp(room, "x");
        em.flush();
        em.clear();

        //then
        System.out.println("updateStamp.getDay() = " + updateStamp.getDay());
        assertThat(updateStamp.getDay()).isEqualTo("oxuuuuuuuuuuuuuuuuuuuuuuuuuuuu");
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
        RoomResponseDto roomDto = roomService.singleRoom(member.getId(), "기상", period, Category.ROUTINE, 2, "");
        Room room = roomRepository.findByNumber(roomDto.getRoomNumber()).orElseThrow(() -> new RuntimeException("해당 챌린지가 없습니다."));

        //when
        roomService.updateStamp(member.getId(), room.getNumber(), new StampDto(room.getNumber(), room.getStamp().getNumber(), "o", room.getStamp().getSuccessCount(), room.getPassCount()));
        roomService.updateStamp(member.getId(), room.getNumber(), new StampDto(room.getNumber(), room.getStamp().getNumber(), "o", room.getStamp().getSuccessCount(), room.getPassCount()));
        roomService.updateStamp(member.getId(), room.getNumber(), new StampDto(room.getNumber(), room.getStamp().getNumber(), "x", room.getStamp().getSuccessCount(), room.getPassCount()));

        em.flush();
        em.clear();

        //then
        assertThat(room.getStamp().getSuccessCount()).isEqualTo(2);
        assertThat(room.getStamp().getDay()).isEqualTo("ooxuuuuuuuuuuuuuuuuuuuuuuuuuuu");
    }

    //기간
    Period period = new Period(LocalDate.now(),30);
}