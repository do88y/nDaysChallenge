package challenge.nDaysChallenge.dto.response.jwt;

import lombok.*;

@NoArgsConstructor
@Getter
public class TokenResponseDto {

    private String type; //Bearer
    private String accessToken;
    private String refreshToken;
    private long accessTokenExpireTime;

    private long refreshTokenExpireTime;

    @Builder
    public TokenResponseDto(String type, String accessToken, String refreshToken, long accessTokenExpireTime, long refreshTokenExpireTime) {
        this.type = type;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireTime = accessTokenExpireTime;
        this.refreshTokenExpireTime = refreshTokenExpireTime;
    }
}
