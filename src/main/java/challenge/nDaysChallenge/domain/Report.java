package challenge.nDaysChallenge.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Report {

    @Id @GeneratedValue
    @Column(name = "report_number")
    private int number;

    private int dajim_number;

    private int comment_number;

    private int member_number;

    private int cause;

    private boolean dajim;

    private String content;

}
