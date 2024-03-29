package challenge.nDaysChallenge.domain.room;


import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.member.Member;
import lombok.*;

import javax.persistence.*;

import static lombok.AccessLevel.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Getter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_number")
    protected Long number;

    @Column(name = "room_name", length = 30)
    protected String name;

    @Embedded
    protected Period period;

    @Enumerated(EnumType.STRING)
    protected Category category;  //카테고리 [MINDFULNESS, EXERCISE, ROUTINE, ETC]

    @Column(length = 50)
    protected String reward;

    @Enumerated(EnumType.STRING)
    protected RoomType type;  //챌린지 종류 [GROUP, SINGLE]

    @Enumerated(EnumType.STRING)
    protected RoomStatus status;  //챌린지 상태 [CONTINUE, END]

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "stamp_number")
    protected Stamp stamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    protected Member member;

    protected int passCount;


    //==비즈니스 로직==//
    //챌린지 상태 변경
    public void end() {
        this.status = RoomStatus.END;
    }

    @Override
    public String toString() {
        return "Room{" +
                "number=" + number +
                ", name='" + name + '\'' +
                ", period=" + period +
                ", category=" + category +
                ", reward='" + reward + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", passCount=" + passCount +
                '}';
    }
}