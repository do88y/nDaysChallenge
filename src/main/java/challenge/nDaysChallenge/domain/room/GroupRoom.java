package challenge.nDaysChallenge.domain.room;

import challenge.nDaysChallenge.domain.RoomMember;
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

    @OneToMany(mappedBy = "room")
    private List<RoomMember> roomMemberList = new ArrayList<>();

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


    //==생성 메서드==// 챌린지 상태 테스트용
    public static GroupRoom createRoom(String name, Period period, Category category, RoomType type, RoomStatus status, int passCount, String reward, int usedPassCount, int successCount) {
        GroupRoom room = new GroupRoom();
        room.name = name;
        room.period = period;
        room.category = category;
        room.type = type;
        room.status = status;
        room.passCount = passCount;
        room.reward = reward;
        room.usedPassCount = usedPassCount;
        room.successCount = successCount;

        return room;
    }

}