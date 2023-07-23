package challenge.nDaysChallenge.dto.request.Room;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class GroupRoomRequestDto extends RoomRequestDto {

    private Set<Long> groupMembers = new HashSet<>();

    public GroupRoomRequestDto(String name, String category, String reward, int passCount, LocalDate startDate, int totalDays, String type, int successCount, int usedPassCount, Set<Long> groupMembers) {
        this.type = type;
        this.name = name;
        this.category = category;
        this.totalDays = totalDays;
        this.startDate = startDate;
        this.passCount = passCount;
        this.reward = reward;
        this.successCount = successCount;
        this.usedPassCount = usedPassCount;
        this.groupMembers.addAll(groupMembers);
    }
}
