package challenge.nDaysChallenge.dto.request.Room;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomRequestDto {

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


    @Builder
    public RoomRequestDto(String name, String category, String reward, int passCount, LocalDate startDate, Long totalDays, String type, int successCount, int usedPassCount) {
        this.type = type;
        this.name = name;
        this.category = category;
        this.totalDays = totalDays;
        this.startDate = startDate;
        this.passCount = passCount;
        this.reward = reward;
        this.successCount = successCount;
        this.usedPassCount = usedPassCount;
    }
}
