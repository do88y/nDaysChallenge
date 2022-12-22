package challenge.nDaysChallenge.Room;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.room.GroupRoomRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.SingleRoomRepository;
import challenge.nDaysChallenge.service.RoomService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RoomQueryTest {


    @Autowired
    RoomRepository roomRepository;
    @Autowired
    SingleRoomRepository singleRoomRepository;
    @Autowired
    GroupRoomRepository groupRoomRepository;
    @Autowired
    RoomMemberRepository roomMemberRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    RoomService roomService;


    Member member = new Member("user@naver.com", "12345", "nick", 1, 4, Authority.ROLE_USER);


    @Test
    public void 중복_쿼리_테스트() throws Exception {
        //given
        List<SingleRoom> singleRooms = makeSingleRooms();

        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (SingleRoom singleRoom : singleRooms) {
                member.getSingleRooms().add(singleRoom);
                members.add(member);
            }
            memberRepository.saveAll(members);
        }

        //when

        //then
        System.out.println("==============================");
        memberRepository.findAll();

    }

    @Test
    public void find_by_id_single_room() throws Exception {
        //given

        //when
        makeSingleRooms();

        //then
        System.out.println("==============================");
        List<SingleRoom> all = singleRoomRepository.findAll();

    }

    @Test
    void find_member_room() throws Exception {
        //given
        List<GroupRoom> groupRooms = makeGroupRooms();
        List<Member> members = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            members.add(member);

        }

        //when

        //then

    }

    List<SingleRoom> makeSingleRooms() {
        List<SingleRoom> singleRooms = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            SingleRoom singleRoom = SingleRoom.createRoom("기상", new Period(LocalDate.now(),30L), Category.ROUTINE, RoomType.SINGLE, RoomStatus.END, 2, "");
            singleRooms.add(singleRoom);
        }
        singleRoomRepository.saveAll(singleRooms);

        return singleRooms;
    }

    List<GroupRoom> makeGroupRooms() {
        List<GroupRoom> groupRooms = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            GroupRoom groupRoom = GroupRoom.createRoom("명상", new Period(LocalDate.now(), 30L), Category.MINDFULNESS, RoomType.GROUP, RoomStatus.END, 20, "여행");
            groupRooms.add(groupRoom);
        }
        groupRoomRepository.saveAll(groupRooms);

        return groupRooms;
    }
}
