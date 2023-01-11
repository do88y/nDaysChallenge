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
    private Room room;

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "stamp_number")
    private Stamp stamp;


    //==연관관계 메서드==//  RoomMember의 room에 roomNumber값 넣으면서 roomMemberList에도 roomNumber 세팅되게

    public void setMember(Member member) {
        this.member = member;
        member.getRoomMemberList().add(this);
    }

    public void addStamp(Stamp stamp) {
        this.stamp = stamp;
    }
    public void joinRoom(Room groupRoom) {
        this.room = groupRoom;
    }

    //==생성 메서드==//
    public static RoomMember createRoomMember(Member member, Room room, Stamp stamp) {
        RoomMember roomMember = new RoomMember();
        roomMember.joinRoom(room);
        roomMember.setMember(member);
        roomMember.addStamp(stamp);

        return roomMember;
    }

}