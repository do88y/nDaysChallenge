package challenge.nDaysChallenge.domain;

import javax.persistence.*;

@Entity
public class Report {

    @Id @GeneratedValue
    @Column(name = "report_number")
    private int number;

    @ManyToOne
    @JoinColumn(name = "dajim_number")
    private Dajim dajim;

    @ManyToOne
    @JoinColumn(name = "comment_number")
    private Dajim_comment comment;

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    private int cause;

    private boolean isDajim;

    private String content;

}
