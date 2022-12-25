package challenge.nDaysChallenge.dto.response.Room;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomResponseDto {

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

    @Builder
    public RoomResponseDto(Long number, String name, String category, String reward, int passCount, String type, String status, LocalDate startDate, LocalDate endDate, Long totalDays) {
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
    }

}