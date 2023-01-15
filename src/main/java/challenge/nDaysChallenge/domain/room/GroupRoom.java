package challenge.nDaysChallenge.domain.room;

import challenge.nDaysChallenge.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupRoom extends Room {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    @OneToMany(mappedBy = "room")
    private List<RoomMember> roomMemberList = new ArrayList<>();

    @ElementCollection
    @CollectionTable(joinColumns = @JoinColumn(name = "room_number"))
    @Column(name = "stamp_number")
    private Map<String, Long> stamps = new HashMap<>();

    //==생성자==//
    public GroupRoom(Member member, String name, Period period, Category category, int passCount, String reward, int usedPassCount, int successCount) {
        this.member = member;
        this.name = name;
        this.period = period;
        this.category = category;
        this.type = RoomType.GROUP;
        this.status = RoomStatus.CONTINUE;
        this.passCount = passCount;
        this.reward = reward;
        this.usedPassCount = usedPassCount;
        this.successCount = successCount;
    }

}