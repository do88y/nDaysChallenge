package challenge.nDaysChallenge.dto.request;

import challenge.nDaysChallenge.domain.dajim.Open;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DajimRequestDto {

    private Long dajimNumber;

    private String content;

    private String open;

    public DajimRequestDto(Long dajimNumber, String content, String open) {
        this.dajimNumber = dajimNumber;
        this.content = content;
        this.open = open;
    }
}
