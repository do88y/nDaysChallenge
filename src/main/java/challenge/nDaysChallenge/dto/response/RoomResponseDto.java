package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.RoomStatus;
import challenge.nDaysChallenge.domain.room.RoomType;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class RoomResponseDto {

    private String name;
    private String category;
    private String reward;
    private int passCount;
    private String type;
    private String status;

    private Long totalDays;

    private Set<Long> groupMembers = new HashSet<>();


    @Builder
    public RoomResponseDto(String name, String category, String reward, int passCount, String type, String status, Long totalDays, Set<Long> groupMembers) {
        this.name = name;
        this.category = category;
        this.reward = reward;
        this.type = type;
        this.passCount = passCount;
        this.totalDays = totalDays;
        for (Long members : groupMembers) {
            this.groupMembers.add(members);
        }
    }

}
