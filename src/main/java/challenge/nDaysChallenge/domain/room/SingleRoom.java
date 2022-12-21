package challenge.nDaysChallenge.domain.room;

import challenge.nDaysChallenge.domain.Member;
import lombok.AccessLevel;
import lombok.Builder;
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
    private Member member;


    //getter
    public Member giveMember() {
        return member;
    }

    //setter
    public void addMember(Member member) {
        this.member = member;
    }

    //==연관관계 메서드==//  SingleRoom의 room에 roomNumber 넣으면서 singleRooms에도 roomNumber 세팅
    public void joinRoom(Room room) {
        this.room = room;
        member.getSingleRooms().add(this);
    }

    //==생성 메서드==//
    public SingleRoom addRoom(Room room, Member member) {
        this.addMember(member);
        this.joinRoom(room);

        return this;
    }

    //==생성자==//
    public SingleRoom(String name, Period period, Category category, int passCount, String reward) {
        this.name = name;
        this.period = period;
        this.category = category;
        this.type = RoomType.SINGLE;
        this.status = RoomStatus.CONTINUE;
        this.passCount = passCount;
        this.reward = reward;
    }


    //==생성 메서드==// 챌린지 상태 테스트용
    public static SingleRoom createRoom(String name, Period period, Category category, RoomType type, RoomStatus status, int passCount, String reward) {
        SingleRoom room = new SingleRoom();
        room.name = name;
        room.period = period;
        room.category = category;
        room.type = type;
        room.status = status;
        room.passCount = passCount;
        room.reward = reward;

        return room;
    }

}