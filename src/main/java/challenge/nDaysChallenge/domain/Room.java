package challenge.nDaysChallenge.domain;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_number")
    private Long number;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<RoomMember> roomMembers = new ArrayList<>();

    @Column(name = "room_name", nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(length = 50)
    private String reward = null;

    @Enumerated(EnumType.STRING)
    private Category category;  //카테고리 [MINDFULNESS, EXERCISE, ROUTINE, ETC]

    private int successCount;

    @Enumerated(EnumType.STRING)
    private RoomStatus status = RoomStatus.CONTINUE;  //챌린지 상태 [CONTINUE, END]

    
    //==연관관계 메서드==//
    public void addRoomMember(RoomMember roomMember) {
        roomMembers.add(roomMember);
        roomMember.setRoom(this);
    }

    //==생성 메서드==// 연관관계 걸면서 세팅
    public static Room createRoom() {
        Room room = new Room();
        for (Object o : ) {
            
        }
    }
}

