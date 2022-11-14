package challenge.nDaysChallenge.controller.dto;

import lombok.*;

@Getter
@Builder
public class JwtDTO {

    private String type; //bearer
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireTime;

}
