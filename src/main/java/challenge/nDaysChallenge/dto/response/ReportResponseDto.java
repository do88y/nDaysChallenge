package challenge.nDaysChallenge.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReportResponseDto {

    private Long report;
    private int cause;
    @JsonProperty
    private boolean isDajim;  //is필드명에 쓰면 안돼서 오류 가능성 있음
    private String content;
    private Long dajim;

    @Builder
    public ReportResponseDto(Long report, int cause, boolean isDajim, String content, Long dajim) {
        this.report = report;
        this.cause = cause;
        this.isDajim = isDajim;
        this.content = content;
        this.dajim = dajim;
    }
}
