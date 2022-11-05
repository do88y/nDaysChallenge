package challenge.nDaysChallenge.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Party {

    @Id @GeneratedValue
    @Column(name = "party_number")
    private int number;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "member_nickname")
    private Member nickname;

    @ManyToOne
    @JoinColumn(name = "room_number")
    private Room room;
}