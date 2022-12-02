package challenge.nDaysChallenge.dto;

import lombok.*;

@NoArgsConstructor
@Getter
public class TokenDto {

    private String type; //bearer
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpireTime;

    @Builder
    public TokenDto(String type, String accessToken, String refreshToken, Long accessTokenExpireTime) {
        this.type = type;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireTime = accessTokenExpireTime;
    }
}
