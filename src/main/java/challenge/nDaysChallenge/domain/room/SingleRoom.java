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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_number")
    private static Member member;

    public static void addMember(Member member) {
        SingleRoom.member = member;
    }

    public static Member giveMember() {
        return member;
    }


    //==연관관계 메서드==//  SingleRoom의 member에 memberNumber 넣으면서 singleRooms에도 memberNumber 세팅
    public void joinRoom(Long roomNumber) {
        this.number = roomNumber;
        member.getSingleRooms().add(this);
    }

    //==생성 메서드==//
    public static SingleRoom addRoom(Room room) {
        SingleRoom singleRoom = new SingleRoom();
        singleRoom.joinRoom(room.getNumber());

        return singleRoom;
    }

}