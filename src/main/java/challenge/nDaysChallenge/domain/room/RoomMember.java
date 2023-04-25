package challenge.nDaysChallenge.domain.room;

import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.member.Member;
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

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "room_number")
    private GroupRoom room;

    @OneToOne(fetch = LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "stamp_number")
    private Stamp stamp;

    //==연관관계 메서드==//  RoomMember의 room에 값 넣으면서 roomMemberList에도 세팅되게
    public void addRoom(GroupRoom room) {
        this.room = room;
        room.getRoomMemberList().add(this);
    }

    //==생성 메서드==//
    public static RoomMember createRoomMember(Member member, GroupRoom room, Stamp stamp) {
        RoomMember roomMember = new RoomMember();
        roomMember.member = member;
        roomMember.addRoom(room);
        roomMember.stamp = stamp;

        return roomMember;
    }

    public void deleteConnection(){
        this.member=null;
        this.room=null;
        this.stamp=null;
    }

}