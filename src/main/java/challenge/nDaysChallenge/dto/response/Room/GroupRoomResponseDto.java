package challenge.nDaysChallenge.dto.response.Room;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupRoomResponseDto {

    private Long roomNumber;
    private String name;
    private String category;
    private String reward;
    private int passCount;
    private String type;
    private String status;
    private Long stamp;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Long totalDays;

    private Set<Long> groupMembers = new HashSet<>();

    @Builder
    public GroupRoomResponseDto(Long roomNumber, String name, String category, String reward, int passCount, String type, String status, Long stamp, LocalDate startDate, LocalDate endDate, Long totalDays, Set<Long> groupMembers) {
        this.roomNumber = roomNumber;
        this.name = name;
        this.category = category;
        this.reward = reward;
        this.type = type;
        this.status = status;
        this.stamp = stamp;
        this.passCount = passCount;
        this.totalDays = totalDays;
        this.startDate = startDate;
        this.endDate = endDate;
        for (Long members : groupMembers) {
            this.groupMembers.add(members);
        }
    }

}
