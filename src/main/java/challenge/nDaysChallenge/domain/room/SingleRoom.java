package challenge.nDaysChallenge.domain.room;

import challenge.nDaysChallenge.domain.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SingleRoom extends Room {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_number")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_number")
    private static Member member;


    //setter
    public static void addMember(Member member) {
        SingleRoom.member = member;
    }

    //getter
    public static Member giveMember() {
        return member;
    }

    //==연관관계 메서드==//  SingleRoom의 member에 memberNumber 넣으면서 singleRooms에도 memberNumber 세팅
    public void joinRoom(Room roomNumber) {
        this.room = room;
        member.getSingleRooms().add(this);
    }

    //==생성 메서드==//
    public static SingleRoom addRoom(Room room, Member member) {
        SingleRoom singleRoom = new SingleRoom();
        singleRoom.addMember(member);
        singleRoom.joinRoom(room);

        return singleRoom;
    }

}