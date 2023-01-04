package challenge.nDaysChallenge.dto.request.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class TokenRequestDto {

    private String accessToken;
    private String refreshToken;

}
