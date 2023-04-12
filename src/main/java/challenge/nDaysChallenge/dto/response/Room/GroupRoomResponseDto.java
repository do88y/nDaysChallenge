package challenge.nDaysChallenge.dto.response.room;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupRoomResponseDto {

    private Long roomNumber;
    private String type;
    private String name;
    private String category;
    private Long totalDays;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private int passCount;
    private String reward;
    private String status;


    private Set<Long> groupMembers = new HashSet<>();

    @Builder
    public GroupRoomResponseDto(Long roomNumber, String name, String category, String reward, int passCount, String type, String status, LocalDate startDate, LocalDate endDate, Long totalDays, Set<Long> groupMembers) {
        this.roomNumber = roomNumber;
        this.type = type;
        this.name = name;
        this.category = category;
        this.totalDays = totalDays;
        this.startDate = startDate;
        this.endDate = endDate;
        this.passCount = passCount;
        this.reward = reward;
        this.status = status;
        this.groupMembers.addAll(groupMembers);
    }

}
