package challenge.nDaysChallenge.dto.request.Room;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomRequestDTO {

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

    private Long memberNumber;

    @Builder
    public RoomRequestDTO(String name, String category, String reward, int passCount, LocalDate startDate, Long totalDays, String type, int successCount, int usedPassCount, Long memberNumber) {
        this.name = name;
        this.category = category;
        this.reward = reward;
        this.passCount = passCount;
        this.startDate = startDate;
        this.totalDays = totalDays;
        this.type = type;
        this.successCount = successCount;
        this.usedPassCount = usedPassCount;
        this.memberNumber = memberNumber;
    }
}
