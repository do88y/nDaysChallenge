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
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public void 관리자_status_쿼리() throws Exception {
        //given
        RoomSearch roomSearch = new RoomSearch("END", null);
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminRoomResponseDto> results = roomRepository.findRoomAdmin(roomSearch, pageRequest);

        //개인과 그룹 챌린지 각각 3개씩 조회되는 문제, 그룹챌린지 중복 조회되는 문제(distinct 적용)
        for (AdminRoomResponseDto result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    public void 관리자_memberId_쿼리() throws Exception {
        //given
        RoomSearch roomSearch = new RoomSearch(null, "aaaa@naver.com");
        PageRequest pageRequest = PageRequest.of(0, 3);

        List<AdminRoomResponseDto> results = roomRepository.findRoomAdmin(roomSearch, pageRequest);

        for (AdminRoomResponseDto result : results) {
            System.out.println("result = " + result);
        }
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
//        roomService.groupRoom(member1, "커밋", new Period(LocalDate.now(), 15), 0, "", new Set<Long>(member2.getNumber()));

        Room findRoom2 = roomRepository.findByNumber(room2.getRoomNumber()).get();

        findRoom2.end();

        //when
        RoomSearch findAll = new RoomSearch(null, null);
        RoomSearch findById = new RoomSearch(null, "user1@naver.com");
        RoomSearch findByStatusId = new RoomSearch("END", "user1@naver.com");
        PageRequest pageRequest = PageRequest.of(0, 2);

        Page<AdminRoomResponseDto> allResults = roomRepository.findRoomAdmin(findAll, pageRequest);
        Page<AdminRoomResponseDto> userResults = roomRepository.findRoomAdmin(findById, pageRequest);
        Page<AdminRoomResponseDto> finishedResults = roomRepository.findRoomAdmin(findByStatusId, pageRequest);

        //then
        assertThat(allResults.getSize()).isEqualTo(2);
        assertThat(allResults)
                .extracting("name")
                .contains("기상", "명상");

        assertThat(userResults.getSize()).isEqualTo(2);
        assertThat(userResults)
                .extracting("name")
                .contains("기상", "명상");

        assertThat(finishedResults)
                .extracting("name")
                .containsExactly("명상");
    }*/
}
