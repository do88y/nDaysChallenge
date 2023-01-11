package challenge.nDaysChallenge.dto.response.jwt;

import lombok.*;

@NoArgsConstructor
@Getter
public class TokenResponseDto {

    private String type; //Bearer
    private String accessToken;
    private String refreshToken;
    private long accessTokenExpireTime;

    @Builder
    public TokenResponseDto(String type, String accessToken, String refreshToken, long accessTokenExpireTime) {
        this.type = type;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireTime = accessTokenExpireTime;
    }
}
