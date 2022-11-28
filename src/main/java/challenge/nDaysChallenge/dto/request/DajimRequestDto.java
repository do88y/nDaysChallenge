package challenge.nDaysChallenge.dto.request;

import challenge.nDaysChallenge.domain.dajim.Open;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DajimRequestDto {

    private String content;

    private String open;

}
