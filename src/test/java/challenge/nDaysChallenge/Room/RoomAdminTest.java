package challenge.nDaysChallenge.Room;

import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.Period;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.response.room.RoomResponseDto;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.RoomSearch;
import challenge.nDaysChallenge.service.AdminService;
import challenge.nDaysChallenge.service.RoomService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
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
    public void 상태와_ID로_검색() throws Exception {
        //given
        Member member = Member.builder()
                .id("user@naver.com")
                .pw("12345")
                .nickname("abc")
                .authority(Authority.ROLE_USER)
                .build();
        em.persist(member);

        RoomResponseDto room1 = roomService.singleRoom(member, "기상", new Period(LocalDate.now(), 15L), Category.ROUTINE, 2, "");
        RoomResponseDto room2 = roomService.singleRoom(member, "기상", new Period(LocalDate.now(), 15L), Category.ROUTINE, 2, "");
        RoomResponseDto room3 = roomService.singleRoom(member, "기상", new Period(LocalDate.now(), 15L), Category.ROUTINE, 2, "");

        Room findRoom1 = roomRepository.findByNumber(room1.getRoomNumber()).get();
        Room findRoom2 = roomRepository.findByNumber(room2.getRoomNumber()).get();
        Room findRoom3 = roomRepository.findByNumber(room3.getRoomNumber()).get();

        findRoom3.end();

        //when

        List<Room> allRooms = adminService.findRooms(new RoomSearch(null, null));
        List<Room> rooms = adminService.findRooms(new RoomSearch(null, "user@naver.com"));
        List<Room> finishedRooms = adminService.findRooms(new RoomSearch("END", null));

        //then
        assertThat(allRooms).contains(findRoom1, findRoom2);

        assertThat(rooms.size()).isEqualTo(3);
        assertThat(rooms).contains(findRoom1, findRoom2);

        assertThat(finishedRooms.get(0)).isEqualTo(findRoom3);
    }
}
