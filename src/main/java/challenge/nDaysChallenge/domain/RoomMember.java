package challenge.nDaysChallenge.domain;


import challenge.nDaysChallenge.domain.room.GroupRoom;
import challenge.nDaysChallenge.exception.NotEnoughRoomException;
import challenge.nDaysChallenge.domain.room.Room;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_member_number")
    private Long number;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_number")
    private Member member;

    @ManyToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_number")
    private GroupRoom room;

    @Column(name = "member_room_count")
    private int roomCount = 0;  //챌린지 5개 제한


    //==연관관계 메서드==//  RoomMember의 room에 roomNumber값 넣으면서 roomMemberList에도 roomNumber 세팅되게

    public void setMember(Member member) {
        this.member = member;
        member.getRoomMemberList().add(this);
    }
    public void joinRoom(GroupRoom groupRoom) {
        this.room = groupRoom;
        groupRoom.getRoomMemberList().add(this);
    }

    //==생성 메서드==//
    public static RoomMember createRoomMember(Member member, GroupRoom room) {
        RoomMember roomMember = new RoomMember();
        roomMember.setMember(member);
        roomMember.joinRoom(room);
        roomMember.addCount();

        return roomMember;
    }


    //==비즈니스 로직==// 객체지향적 관점에서 데이터가 있는 곳에 비지니스 메서드가 있는 것이 좋음

    /**
     * 챌린지 삭제
     */
/*    public void delete() {
        if (room.getType() == RoomType.GROUP)
        for (RoomMember roomMember : roomMemberList) {
            roomMember.delete();
        }
    }*/

    /**
     * roomCount +1
     */
    public void addCount() {
        if (roomCount >= 5) {
            throw new NotEnoughRoomException("챌린지는 최대 5개까지만 생성할 수 있습니다.");
        }else {
            this.roomCount += 1;
        }
    }

    /**
     * roomCount -1
     */
    public void reduceCount() {
        int restRoom = this.roomCount - 1;
        if (restRoom < 0) {
            throw new IllegalStateException("no more room");
        } else {
            this.roomCount = restRoom;
        }
    }

}