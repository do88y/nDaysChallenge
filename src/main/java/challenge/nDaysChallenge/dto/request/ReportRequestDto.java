package challenge.nDaysChallenge.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportRequestDto {

    private Long dajim;
    private int cause;
    @JsonProperty
    private boolean isDajim;
    private String content;

    public boolean getIsDajim() {
        return isDajim;
    }


    @Builder
    public ReportRequestDto(Long dajim, int cause, boolean isDajim, String content) {
        this.dajim = dajim;
        this.cause = cause;
        this.isDajim = isDajim;
        this.content = content;
    }
}
