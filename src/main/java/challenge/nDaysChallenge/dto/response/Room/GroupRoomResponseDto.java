package challenge.nDaysChallenge.dto.response.Room;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupRoomResponseDto {

    private Long number;
    private String name;
    private String category;
    private String reward;
    private int passCount;
    private String type;
    private String status;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Long totalDays;

    private Set<Long> groupMembers = new HashSet<>();

    @Builder
    public GroupRoomResponseDto(Long number, String name, String category, String reward, int passCount, String type, String status, LocalDate startDate, LocalDate endDate, Long totalDays, Set<Long> groupMembers) {
        this.number = number;
        this.name = name;
        this.category = category;
        this.reward = reward;
        this.type = type;
        this.status = status;
        this.passCount = passCount;
        this.totalDays = totalDays;
        this.startDate = startDate;
        this.endDate = endDate;
        for (Long members : groupMembers) {
            this.groupMembers.add(members);
        }
    }

}
