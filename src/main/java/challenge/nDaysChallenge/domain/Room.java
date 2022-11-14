package challenge.nDaysChallenge.domain;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_number")
    private Long number;

    @OneToMany(mappedBy = "member")
    private List<RoomMember> roomMembers = new ArrayList<>();

    @Column(name = "room_name", nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(length = 50)
    private String reward;

    @Enumerated(EnumType.STRING)
    private Category category;  //카테고리 [MINDFULNESS, EXERCISE, ROUTINE, ETC]

    @Enumerated(EnumType.STRING)
    private RoomStatus status;  //챌린지 상태 [CONTINUE, END]

    private int successCount;
    private int passCount;

    @Transient
    private int totalDays;
    @Transient
    private int failCount;


    //==연관관계 메서드==//
    public void addRoomMember(RoomMember roomMember) {
        roomMembers.add(roomMember);
        roomMember.setRoom(this);
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

    //==생성 메서드==// 빌더패턴 이용  **단체인 경우 생성하면서 count -1
    @Builder
    public Room(String name, LocalDateTime startDate, LocalDateTime endDate, String reward, Category category, RoomStatus status, int passCount, RoomMember... roomMembers) {
        this.name = name;
        this.startDate = LocalDateTime.now();
        this.endDate = startDate.plusDays(30);  //파라미터 받는 변수로 수정해야 함
        this.category = category;
        this.status = RoomStatus.CONTINUE;
        this.passCount = 0;
        for (RoomMember roomMember : roomMembers) {
            this.addRoomMember(roomMember);
        }


    }

    //==비즈니스 로직==//

    /**
     * 챌린지 삭제(패스 횟수 초과)
     */
    public void delete() {
        if (passCount > failCount) {
            for (RoomMember roomMember : roomMembers) {
                roomMember.delete();
            }
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

