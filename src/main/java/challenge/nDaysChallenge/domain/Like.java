package challenge.nDaysChallenge.domain;

import javax.persistence.*;

@Entity
public class Like {

    @Id @GeneratedValue
    @Column(name = "like_number")
    private int number;

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "dajim_number")
    private Dajim dajim;

}
