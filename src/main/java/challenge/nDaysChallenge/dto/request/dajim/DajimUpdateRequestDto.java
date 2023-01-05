package challenge.nDaysChallenge.dto.request.dajim;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DajimUpdateRequestDto {

    private Long dajimNumber;

    private String content;

    private String open;

}
