package challenge.nDaysChallenge.report;

import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.Report;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.Period;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import challenge.nDaysChallenge.repository.ReportRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ReportServiceTest {

    @Autowired ReportRepository reportRepository;
    @Autowired DajimRepository dajimRepository;

    @Test
    public void 신고_생성() throws Exception {
        //given
        Member member = new Member("user@naver.com","12345","userN",1,4, Authority.ROLE_USER);
        SingleRoom room = new SingleRoom("기상", new Period(LocalDate.now(), 10L), Category.ROUTINE, 2, "", 0, 0);
        Dajim newDajim = Dajim.builder()
                .room(room)
                .member(member)
                .content("다짐 내용")
                .open(Open.PUBLIC)
                .build();
        dajimRepository.save(newDajim);

        //when
        Report report = Report.createReport(newDajim, 1, true, "저를 욕해요");
        Report saveReport = reportRepository.save(report);

        //then
        assertThat(saveReport.getNumber()).isEqualTo(report.getNumber());

    }
}
