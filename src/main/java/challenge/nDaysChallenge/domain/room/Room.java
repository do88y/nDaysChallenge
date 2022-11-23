package challenge.nDaysChallenge.domain.room;


import challenge.nDaysChallenge.domain.RoomMember;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static lombok.AccessLevel.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PROTECTED)
@Getter
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_number")
    private Long number;

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
    private int successCount;

    @Transient
    private int failCount;



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
    public static Room createRoom(String name, LocalDateTime startDate, LocalDateTime endDate, String reward, Category category, RoomStatus status, RoomMember... roomMembers) {
        Room room = new Room(List.of(roomMembers), name);

        room.name = name;
        room.startDate = LocalDateTime.now();
        room.endDate = startDate.plusDays(30);
        room.reward = reward;

        for (RoomMember roomMember : roomMembers) {
            room.addRoomMember(roomMember);
        }


        room.status = RoomStatus.CONTINUE;
        return room;
    }*/

    //==비즈니스 로직==//

    /**
     * 챌린지 삭제
     */
    public void delete() {
        if (type == RoomType.GROUP) {

        }
    }

    /**
     * 버튼 클릭 시
     */
    public void pushButton() {
        if (true) {
            successCount += 1;
        } else if (false) {
            failCount += 1;
            passCount += 1;
        }
    }

}

