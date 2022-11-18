package challenge.nDaysChallenge.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class JwtDTO {

    private String type; //bearer
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireTime;

}
