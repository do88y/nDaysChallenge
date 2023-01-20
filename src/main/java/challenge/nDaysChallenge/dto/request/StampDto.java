package challenge.nDaysChallenge.dto.request;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StampDto {

    public Long roomNumber;
    public Long stampNumber;
    public String day;

    public int successCount;
    public int usedPassCount;

    @Builder
    public StampDto(Long roomNumber, Long stampNumber, String day, int successCount, int usedPassCount) {
        this.roomNumber = roomNumber;
        this.stampNumber = stampNumber;
        this.day = day;
        this.successCount = successCount;
        this.usedPassCount = usedPassCount;
    }
}
