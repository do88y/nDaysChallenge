package challenge.nDaysChallenge.domain.room;


import lombok.*;
import net.minidev.json.JSONUtil;

import javax.persistence.*;

import java.util.Timer;
import java.util.TimerTask;

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

    protected int passCount;
    private int usedPassCount;
    private int successCount;




/*
    //==빌더패턴 생성자==//
    @Builder
    public Room(String name, Period period, Category category, RoomType type, int passCount, String reward) {
        this.name = name;
        this.period = period;
        this.category = category;
        this.type = type;
        this.status = RoomStatus.CONTINUE;
        this.passCount = passCount;
        this.reward = reward;
    }
*/


}