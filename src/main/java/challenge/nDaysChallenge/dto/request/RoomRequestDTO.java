package challenge.nDaysChallenge.dto.request;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.RoomStatus;
import challenge.nDaysChallenge.domain.room.RoomType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomRequestDTO {

    private String name;
    private Category category;
    private String reward;
    private int passCount;
    private RoomType type;
    private RoomStatus status;

    private Long totalDays;

    private Member member;
    private List<Member> members = new ArrayList<>();


/*
    public  RoomRequestDTO(String name, Category category, String reward, int passCount, Long totalDays) {
        this.name = name;
        this.category = category;
        this.reward = reward;
        this.passCount = passCount;
        this.totalDays = totalDays;
        this.type = RoomType.SINGLE;
        this.status = RoomStatus.CONTINUE;
    }
*/

    @Builder
    public RoomRequestDTO(String name, Category category, String reward, int passCount, Long totalDays, Member member, Member... members) {
        this.name = name;
        this.category = category;
        this.reward = reward;
        this.passCount = passCount;
        this.totalDays = totalDays;
        this.member = member;
        for (Member mem : members) {
            this.members.add(mem);
        }
    }
}
