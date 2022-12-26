package challenge.nDaysChallenge.dto.request.Room;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupRoomRequestDto {

    private String name;
    private String category;
    private String reward;
    private int passCount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    private Long totalDays;
    private String type;
    private int successCount;
    private int usedPassCount;

    private Set<Long> groupMembers = new HashSet<>();

    @Builder
    public GroupRoomRequestDto(String name, String category, String reward, int passCount, LocalDate startDate, Long totalDays, String type, int successCount, int usedPassCount, Set<Long> groupMembers) {
        this.name = name;
        this.category = category;
        this.reward = reward;
        this.passCount = passCount;
        this.startDate = startDate;
        this.totalDays = totalDays;
        this.type = type;
        this.successCount = successCount;
        this.usedPassCount = usedPassCount;
        for (Long members : groupMembers) {
            this.groupMembers.add(members);
        }
    }
}
