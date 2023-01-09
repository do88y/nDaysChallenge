package challenge.nDaysChallenge.domain.room;

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

    @OneToMany(mappedBy = "room")
    private List<RoomMember> roomMemberList = new ArrayList<>();
    @ElementCollection
    private Map<String, Long> stamps = new HashMap<>();

    public void addStamps(Map<String, Long> stamps) {
        this.stamps = stamps;
    }

    //==생성자==//
    public GroupRoom(String name, Period period, Category category, int passCount, String reward, int usedPassCount, int successCount) {
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