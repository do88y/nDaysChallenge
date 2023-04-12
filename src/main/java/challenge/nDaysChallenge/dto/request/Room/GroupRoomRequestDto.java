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

    private String type;
    private String name;
    private String category;
    private Long totalDays;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    private int passCount;
    private String reward;

    private int successCount;
    private int usedPassCount;

    private Set<Long> groupMembers = new HashSet<>();

    @Builder
    public GroupRoomRequestDto(String name, String category, String reward, int passCount, LocalDate startDate, Long totalDays, String type, int successCount, int usedPassCount, Set<Long> groupMembers) {
        this.type = type;
        this.name = name;
        this.category = category;
        this.totalDays = totalDays;
        this.startDate = startDate;
        this.passCount = passCount;
        this.reward = reward;
        this.successCount = successCount;
        this.usedPassCount = usedPassCount;
        this.groupMembers.addAll(groupMembers);
    }
}
