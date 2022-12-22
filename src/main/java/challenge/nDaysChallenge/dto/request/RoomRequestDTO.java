package challenge.nDaysChallenge.dto.request;

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

    @DateTimeFormat(pattern = "yyyy.MM.dd")
    private LocalDate startDate;
    private Long totalDays;
    private String type;

    private Long memberNumber;
    private Set<Long> groupMembers = new HashSet<>();

    //필수
    public RoomRequestDTO(String name, String category, int passCount, LocalDate startDate, Long totalDays, String type, Long memberNumber) {
        this.name = name;
        this.category = category;
        this.passCount = passCount;
        this.startDate = startDate;
        this.totalDays = totalDays;
        this.type = type;
        this.memberNumber = memberNumber;
    }

    @Builder
    public RoomRequestDTO(String name, String category, String reward, int passCount, LocalDate startDate, Long totalDays, String type, Long memberNumber, Set<Long> groupMembers) {
        this.name = name;
        this.category = category;
        this.reward = reward;
        this.passCount = passCount;
        this.startDate = startDate;
        this.totalDays = totalDays;
        this.type = type;
        this.memberNumber = memberNumber;
        for (Long groupMember : groupMembers) {
            this.groupMembers.add(groupMember);
        }
    }
}
