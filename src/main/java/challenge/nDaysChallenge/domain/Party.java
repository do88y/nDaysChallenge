package challenge.nDaysChallenge.domain;


import javax.persistence.*;

@Entity
public class Party {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_number")
    private Long number;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "room_number")
    private Room room;
}