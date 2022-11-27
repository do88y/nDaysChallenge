package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.RoomStatus;
import challenge.nDaysChallenge.domain.room.RoomType;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponseDto {

    private String name;
    private Category category;
    private String reward;
    private int passCount;
    private RoomType type;
    private RoomStatus status;

    private Long totalDays;

    private Member member;
    private List<Member> members = new ArrayList<>();


    @Builder
    public RoomResponseDto(String name, Category category, String reward, RoomType type, RoomStatus status, int passCount, Long totalDays, Member member, Member... members) {
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

    public RoomResponseDto(String name, Category category, String reward, int passCount, Long totalDays) {

    }
}
