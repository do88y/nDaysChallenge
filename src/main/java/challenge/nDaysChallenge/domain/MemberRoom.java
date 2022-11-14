package challenge.nDaysChallenge.domain;


import javax.persistence.*;

@Entity
public class MemberRoom {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_room_number")
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_number")
    private Room room;

    @Column(name = "member_room_count")
    private int count;  //챌린지 5개 제한
}