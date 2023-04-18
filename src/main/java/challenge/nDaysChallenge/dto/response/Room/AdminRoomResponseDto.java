package challenge.nDaysChallenge.dto.response.room;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminRoomResponseDto {

    private Long roomNumber;
    private String name;
    private String type;
    private String category;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String memberId;

    @QueryProjection
    @Builder
    public AdminRoomResponseDto(Long roomNumber, String name, String type, String category, String status, LocalDate startDate, LocalDate endDate, String memberId) {
        this.roomNumber = roomNumber;
        this.name = name;
        this.type = type;
        this.category = category;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.memberId = memberId;
    }
}
