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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_number")
    private Member member;

    //==연관관계 메서드==//  SingleRoom의 member에 memberNumber 넣으면서 singleRooms에도 memberNumber 세팅
    public void addMember(Member member) {
        this.member = member;
        member.getSingleRooms().add(this);
    }

    //==생성 메서드==//
    public static SingleRoom addRoomMember(Member member) {
        SingleRoom singleRoom = new SingleRoom();
        singleRoom.addMember(member);

        return singleRoom;
    }

}
