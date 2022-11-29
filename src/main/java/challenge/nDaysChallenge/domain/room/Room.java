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
    private String name;

    @Embedded
    private Period period;

    @Enumerated(EnumType.STRING)
    private Category category;  //카테고리 [MINDFULNESS, EXERCISE, ROUTINE, ETC]

    @Column(length = 50)
    private String reward;

    @Enumerated(EnumType.STRING)
    private RoomType type;  //챌린지 종류 [GROUP, SINGLE]

    @Enumerated(EnumType.STRING)
    private RoomStatus status;  //챌린지 상태 [CONTINUE, END]

    private int passCount = 0;
    private int usedPassCount;
    private int successCount;




    //==생성 메서드==// 빌더패턴 이용  **단체인 경우 생성하면서 count -1
    @Builder
    public Room(String name, Period period, Category category, RoomType type, int passCount) {
        this.name = name;
        this.period = period;
        this.category = category;
        this.type = type;
        this.status = RoomStatus.CONTINUE;
        this.passCount = passCount;

    }

/*
    //==생성 메서드==// 생성자 이용
    public static Room createRoom(String name, Period period, Category category, RoomType type, int passCount) {
        Room room = new Room();
        room.name = name;
        room.period = period;
        room.category = category;
        room.type = type;
        room.status = RoomStatus.CONTINUE;
        room.passCount = passCount;

        return room;
    }
*/

}