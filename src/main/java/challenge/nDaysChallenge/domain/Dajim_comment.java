package challenge.nDaysChallenge.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Dajim_comment {

    @Id @GeneratedValue
    @Column(name = "comment_number")
    private int number;

    @ManyToOne
    @JoinColumn(name = "dajim_number")
    private Dajim dajim;

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    private String content;

}
