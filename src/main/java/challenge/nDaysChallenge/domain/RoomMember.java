package challenge.nDaysChallenge.domain;


import challenge.nDaysChallenge.exception.NotEnoughRoomException;
import lombok.Builder;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
public class RoomMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_member_number")
    private Long number;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_number")
    private Room room;

    @Column(name = "member_room_count")
    private int count;  //챌린지 5개 제한


    //==연관관계 메서드==//
    public void setRoom(Room room) {
        this.room = room;
        room.getRoomMembers().add(this);
    }

    //==생성 메서드==//
    @Builder
    public RoomMember(Member member, Room room, int count) {
        this.member = member;
        this.room = room;
        this.count = 0;
    }


    //==비즈니스 로직==// 객체지향적 관점에서 데이터가 있는 곳에 비지니스 메서드가 있는 것이 좋음

    /**
     * room +1
     */
    public void add() {
        if (count > 5) {
            throw new IllegalStateException("챌린지는 최대 5개까지만 생성할 수 있습니다.");
        }
        this.count += 1;
    }

    /**
     * room -1
     */
    public void reduce() {
        int restRoom = this.count - 1;
        if (restRoom < 0) {
            throw new NotEnoughRoomException("no more room");
        }
        this.count = restRoom;
    }

    public void delete() {

    }
}