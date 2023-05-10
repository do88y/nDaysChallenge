package challenge.nDaysChallenge.Room;

import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.Period;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.response.ReportResponseDto;
import challenge.nDaysChallenge.dto.response.room.AdminRoomResponseDto;
import challenge.nDaysChallenge.dto.response.room.RoomResponseDto;
import challenge.nDaysChallenge.repository.report.ReportRepository;
import challenge.nDaysChallenge.repository.report.ReportSearch;
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
import org.springframework.security.test.context.support.WithMockUser;
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
    @Autowired ReportRepository reportRepository;
    @Autowired AdminService adminService;

    @Test
    public void 챌린지_status_검색_쿼리() throws Exception {
        //given
        RoomSearch roomSearch = new RoomSearch("SINGLE", "END", null);
        PageRequest pageRequest = PageRequest.of(0, 3);

        Page<AdminRoomResponseDto> results = roomRepository.findSingleRoomAdmin(roomSearch, pageRequest);

        //개인과 그룹 챌린지 각각 3개씩 조회되는 문제, 그룹챌린지 중복 조회되는 문제(distinct 적용)
        for (AdminRoomResponseDto result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    public void 챌린지_memberId_검색_쿼리() throws Exception {
        //given
        RoomSearch roomSearch = new RoomSearch("SINGLE", null, "aaaa@naver.com");
        PageRequest pageRequest = PageRequest.of(0, 3);

        Page<AdminRoomResponseDto> results = roomRepository.findSingleRoomAdmin(roomSearch, pageRequest);

        for (AdminRoomResponseDto result : results) {
            System.out.println("result = " + result);
        }
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void 챌린지_상태와_ID로_검색() throws Exception {
        //given
        Member member1 = Member.builder()
                .id("user1@naver.com")
                .pw("12345")
                .nickname("abc")
                .authority(Authority.ROLE_ADMIN)
                .build();
        Member member2 = Member.builder()
                .id("user2@naver.com")
                .pw("12345")
                .nickname("abc")
                .authority(Authority.ROLE_ADMIN)
                .build();
        em.persist(member1);
        em.persist(member2);

        RoomResponseDto room1 = roomService.singleRoom(member1.getId(), "기상", new Period(LocalDate.now(), 15), Category.ROUTINE, 2, "");
        RoomResponseDto room2 = roomService.singleRoom(member1.getId(), "명상", new Period(LocalDate.now(), 15), Category.ROUTINE, 2, "");
        RoomResponseDto room3 = roomService.singleRoom(member2.getId(), "운동", new Period(LocalDate.now(), 15), Category.ROUTINE, 2, "");

//        roomService.groupRoom(member1, "커밋", new Period(LocalDate.now(), 15), 0, "", new Set<Long>(member2.getNumber()));

        Room findRoom1 = roomRepository.findByNumber(room1.getRoomNumber()).get();
        Room findRoom2 = roomRepository.findByNumber(room2.getRoomNumber()).get();
        Room findRoom3 = roomRepository.findByNumber(room3.getRoomNumber()).get();

        findRoom2.end();

        //when
        RoomSearch findAll = new RoomSearch("SINGLE", null, null);
        RoomSearch findById = new RoomSearch("SINGLE", null, "user1@naver.com");
        RoomSearch findByStatusId = new RoomSearch("SINGLE", "END", "user1@naver.com");
        PageRequest pageRequest = PageRequest.of(0, 2);

        Page<AdminRoomResponseDto> allResults = roomRepository.findSingleRoomAdmin(findAll, pageRequest);
        Page<AdminRoomResponseDto> userResults = roomRepository.findSingleRoomAdmin(findById, pageRequest);
        Page<AdminRoomResponseDto> finishedResults = roomRepository.findSingleRoomAdmin(findByStatusId, pageRequest);

        //then
        assertThat(allResults)
                .extracting("roomNumber")
                .contains(room1.getRoomNumber(), room2.getRoomNumber(), room3.getRoomNumber());

        assertThat(userResults.getSize()).isEqualTo(2);
        assertThat(userResults)
                .extracting("roomNumber")
                .contains(room1.getRoomNumber(), room2.getRoomNumber());

        assertThat(finishedResults)
                .extracting("roomNumber")
                .containsExactly(room2.getRoomNumber());
    }

    @Test
    public void 다짐_쿼리_테스트() {
        List<ReportResponseDto> reports = reportRepository.findReports(new ReportSearch(1L));
        for (ReportResponseDto report : reports) {
            System.out.println("report = " + report);
        }
    }
}
