package challenge.nDaysChallenge.dto.response.Room;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomResponseDto {

    private Long roomNumber;
    private String name;
    private String category;
    private String reward;
    private int passCount;
    private String type;
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Long stamp;
    private Long totalDays;
    private int successCount;
    private int usedPassCount;

    @Builder
    public RoomResponseDto(Long roomNumber, String name, String category, String reward, int passCount, String type, String status, LocalDate startDate, LocalDate endDate, Long stamp, Long totalDays, int successCount, int usedPassCount) {
        this.roomNumber = roomNumber;
        this.name = name;
        this.category = category;
        this.reward = reward;
        this.type = type;
        this.status = status;
        this.passCount = passCount;
        this.stamp = stamp;
        this.totalDays = totalDays;
        this.startDate = startDate;
        this.endDate = endDate;
        this.successCount = successCount;
        this.usedPassCount = usedPassCount;
    }

}
