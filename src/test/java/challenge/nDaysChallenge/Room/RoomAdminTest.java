package challenge.nDaysChallenge.Room;

import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.Period;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;
import challenge.nDaysChallenge.dto.response.room.RoomResponseDto;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.RoomSearch;
import challenge.nDaysChallenge.service.AdminService;
import challenge.nDaysChallenge.service.RoomService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class RoomAdminTest {

    @Autowired EntityManager em;
    @Autowired RoomRepository roomRepository;
    @Autowired RoomService roomService;
    @Autowired AdminService adminService;

    @Test
    public void 관리자_SingleRoom_status_select_쿼리() throws Exception {
        //given
        RoomSearch roomSearch = new RoomSearch("END", null);
        PageRequest pageRequest = PageRequest.of(0, 3);

        roomRepository.findSingleRoomAdmin(roomSearch, pageRequest);
    }
    @Test
    public void 관리자_SingleRoom_member_select_쿼리() throws Exception {
        //given
        RoomSearch roomSearch = new RoomSearch(null, "aaaa@naver.com");
        PageRequest pageRequest = PageRequest.of(0, 3);

        roomRepository.findSingleRoomAdmin(roomSearch, pageRequest);
    }

    @Test
    public void 관리자_GroupRoom_status_select_쿼리() throws Exception {
        //given
        RoomSearch roomSearch = new RoomSearch("END", null);
        roomRepository.findGroupRoomAdmin(roomSearch);
    }
    @Test
    public void 관리자_GroupRoom_member_select_쿼리() throws Exception {
        //given
        RoomSearch roomSearch = new RoomSearch(null, "aaaa@naver.com");

        roomRepository.findGroupRoomAdmin(roomSearch);
    }

/*    @Test
    public void 상태와_ID로_검색() throws Exception {
        //given
        Member member1 = Member.builder()
                .id("user1@naver.com")
                .pw("12345")
                .nickname("abc")
                .authority(Authority.ROLE_USER)
                .build();
        Member member2 = Member.builder()
                .id("user2@naver.com")
                .pw("12345")
                .nickname("abc")
                .authority(Authority.ROLE_USER)
                .build();
        em.persist(member1);
        em.persist(member2);

        RoomResponseDto room1 = roomService.singleRoom(member1, "기상", new Period(LocalDate.now(), 15), Category.ROUTINE, 2, "");
        RoomResponseDto room2 = roomService.singleRoom(member1, "명상", new Period(LocalDate.now(), 15), Category.ROUTINE, 2, "");
        RoomResponseDto room3 = roomService.singleRoom(member2, "운동", new Period(LocalDate.now(), 15), Category.ROUTINE, 2, "");

        Room findRoom2 = roomRepository.findByNumber(room2.getRoomNumber()).get();

        findRoom2.end();

        //when
        List<Tuple> allResults = adminService.findRooms(new RoomSearch(null, null));
        List<Tuple> userResults = adminService.findRooms(new RoomSearch(null, "user1@naver.com"));
        List<Tuple> finishedResults = adminService.findRooms(new RoomSearch("END", "user1@naver.com"));

        List<Room> allRooms = new ArrayList<>();
        for (Tuple allResult : allResults) {
            Room room = allResult.get(0, Room.class);
            allRooms.add(room);
        }
        List<Room> userRooms = new ArrayList<>();
        for (Tuple userResult : userResults) {
            Room room = userResult.get(0, Room.class);
            userRooms.add(room);
        }
        List<Room> finishedRooms = new ArrayList<>();
        for (Tuple finishedResult : finishedResults) {
            Room room = finishedResult.get(0, Room.class);
            finishedRooms.add(room);
        }

        //then
        assertThat(allRooms)
                .extracting("number")
                .contains(room1.getRoomNumber(), room2.getRoomNumber(), room3.getRoomNumber());

        assertThat(userRooms.size()).isEqualTo(2);
        assertThat(userRooms)
                .extracting("name")
                .contains("기상", "명상");

        assertThat(finishedRooms)
                .extracting("name")
                .containsExactly("명상");
    }*/
}
