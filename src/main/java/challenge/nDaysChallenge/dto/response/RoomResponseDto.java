package challenge.nDaysChallenge.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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

    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private LocalDate endDate;
    private Long totalDays;

    private Set<Long> groupMembers = new HashSet<>();


    //필수값 생성자
    public RoomResponseDto(Long number, String name, String category, int passCount, String type, String status, Long totalDays, LocalDate startDate, LocalDate endDate) {
        this.number = number;
        this.name = name;
        this.category = category;
        this.passCount = passCount;
        this.type = type;
        this.status = status;
        this.totalDays = totalDays;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Builder
    public RoomResponseDto(Long number, String name, String category, String reward, int passCount, String type, String status, LocalDate startDate, LocalDate endDate, Long totalDays, Set<Long> groupMembers) {
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
