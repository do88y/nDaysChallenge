package challenge.nDaysChallenge.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
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
    private String content;
    private Long dajim;

    @QueryProjection
    @Builder
    public ReportResponseDto(Long report, int cause, String content, Long dajim) {
        this.report = report;
        this.cause = cause;
        this.content = content;
        this.dajim = dajim;
    }

    @Override
    public String toString() {
        return "ReportResponseDto{" +
                "report=" + report +
                ", cause=" + cause +
                ", content='" + content + '\'' +
                ", dajim=" + dajim +
                '}';
    }
}
