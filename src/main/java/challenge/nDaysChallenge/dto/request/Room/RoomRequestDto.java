package challenge.nDaysChallenge.dto.request.Room;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomRequestDto {

    private String name;
    private String category;
    private String reward;
    private int passCount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    private Long totalDays;
    private String type;
    private int successCount;
    private int usedPassCount;


    @Builder
    public RoomRequestDto(String name, String category, String reward, int passCount, LocalDate startDate, Long totalDays, String type, int successCount, int usedPassCount) {
        this.name = name;
        this.category = category;
        this.reward = reward;
        this.passCount = passCount;
        this.startDate = startDate;
        this.totalDays = totalDays;
        this.type = type;
        this.successCount = successCount;
        this.usedPassCount = usedPassCount;
    }
}
