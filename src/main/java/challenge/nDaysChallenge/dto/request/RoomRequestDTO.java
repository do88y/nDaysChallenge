package challenge.nDaysChallenge.dto.request;

import lombok.*;

import java.util.*;

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
    private Set<Long> groupMembers = new HashSet<>();


    @Builder
    public RoomRequestDTO(String name, String category, String reward, int passCount, Long totalDays, Long memberNumber, Set<Long> groupMembers) {
        this.name = name;
        this.category = category;
        this.reward = reward;
        this.passCount = passCount;
        this.totalDays = totalDays;
        this.memberNumber = memberNumber;
        for (Long groupMember : groupMembers) {
            this.groupMembers.add(groupMember);
        }
    }
}
