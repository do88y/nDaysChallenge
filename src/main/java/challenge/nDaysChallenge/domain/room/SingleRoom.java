package challenge.nDaysChallenge.domain.room;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.Stamp;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class SingleRoom extends Room {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;


    //==연관관계 메서드==//  SingleRoom의 room에 roomNumber 넣으면서 singleRooms에도 roomNumber 세팅
    public SingleRoom addRoom(Member member, Stamp stamp) {
        this.member = member;
        this.stamp = stamp;
        member.addSingleRooms(this);

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

    public void deleteConnection(){
        this.member=null;
        this.stamp=null;
    }

}