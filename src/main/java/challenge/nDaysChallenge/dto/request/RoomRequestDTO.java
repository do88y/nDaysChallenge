package challenge.nDaysChallenge.dto.request;

import challenge.nDaysChallenge.domain.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomRequestDTO {

    private String name;
    private String category;
    private String reward;
    private int passCount;

    private Long totalDays;
    private String type;

    private Long memberNumber;
    private List<Long> groupMemberNums = new ArrayList<>();


    @Builder
    public RoomRequestDTO(String name, String category, String reward, int passCount, Long totalDays, Long memberNumber, Long... groupMemberNums) {
        this.name = name;
        this.category = category;
        this.reward = reward;
        this.passCount = passCount;
        this.totalDays = totalDays;
        this.memberNumber = memberNumber;
        for (Long groupMemberNum : groupMemberNums) {
            this.groupMemberNums.add(groupMemberNum);
        }
    }
}
