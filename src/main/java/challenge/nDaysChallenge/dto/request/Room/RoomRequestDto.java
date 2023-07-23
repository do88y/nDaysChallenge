package challenge.nDaysChallenge.dto.request.Room;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class RoomRequestDto {

    protected String type;
    protected String name;
    protected String category;
    protected int totalDays;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected LocalDate startDate;
    protected int passCount;
    protected String reward;

    protected int successCount;
    protected int usedPassCount;


    public RoomRequestDto(String name, String category, String reward, int passCount, LocalDate startDate, int totalDays, String type, int successCount, int usedPassCount) {
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
