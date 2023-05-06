package challenge.nDaysChallenge.domain;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Report {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_number")
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dajim_number")
    private Dajim dajim;

    private int cause;

    private String content;



    //==생성 메서드==//
    public static Report createReport(Dajim dajim, int cause, String content) {
        Report report = new Report();
        report.dajim = dajim;
        report.cause = cause;
        report.content = content;

        return report;
    }

}