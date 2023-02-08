package challenge.nDaysChallenge.domain.room;

import challenge.nDaysChallenge.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupRoom extends Room {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    @OneToMany(mappedBy = "room")
    private List<RoomMember> roomMemberList = new ArrayList<>();


    //==생성자==//
    public GroupRoom(Member member, String name, Period period, Category category, int passCount, String reward) {
        this.member = member;
        this.name = name;
        this.period = period;
        this.category = category;
        this.type = RoomType.GROUP;
        this.status = RoomStatus.CONTINUE;
        this.passCount = passCount;
        this.reward = reward;
    }

    public void deleteHostConnection(){
        this.member=null;
        this.stamp=null;
    }

    public void deleteGuestConnection(){
        this.stamp=null;
    }

}