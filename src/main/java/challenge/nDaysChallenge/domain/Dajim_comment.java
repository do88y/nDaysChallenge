package challenge.nDaysChallenge.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Dajim_comment {

    @Id @GeneratedValue
    @Column(name = "comment_number")
    private int number;

    private int dajim_number;

    private int member_number;

    private String content;

}
