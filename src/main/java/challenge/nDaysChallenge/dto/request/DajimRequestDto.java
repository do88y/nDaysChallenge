package challenge.nDaysChallenge.dto.request;

import challenge.nDaysChallenge.domain.dajim.Open;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DajimRequestDto {

    private String content;

    private Open open;

}
