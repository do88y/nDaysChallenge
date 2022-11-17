package challenge.nDaysChallenge.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class JwtRequestDTO {

    private String accessToken;
    private String refreshToken;

}
