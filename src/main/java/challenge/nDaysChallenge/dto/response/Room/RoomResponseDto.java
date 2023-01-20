package challenge.nDaysChallenge.dto.response.Room;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomResponseDto {

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

    private Long stamp;
    private int successCount;
    private int usedPassCount;

    @Builder
    public RoomResponseDto(Long roomNumber, String name, String category, String reward, int passCount, String type, String status, LocalDate startDate, LocalDate endDate, Long stamp, Long totalDays, int successCount, int usedPassCount) {
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
        this.stamp = stamp;
        this.successCount = successCount;
        this.usedPassCount = usedPassCount;
    }

}
