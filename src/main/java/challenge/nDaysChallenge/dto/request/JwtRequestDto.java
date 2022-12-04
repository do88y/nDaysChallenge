package challenge.nDaysChallenge.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class JwtRequestDto {

    private String accessToken;
    private String refreshToken;

}
