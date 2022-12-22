package challenge.nDaysChallenge.dto;

import lombok.*;

@NoArgsConstructor
@Getter
public class TokenDto {

    private String type; //Bearer
    private String accessToken;
    private String refreshToken;
    private long accessTokenExpireTime;

    @Builder
    public TokenDto(String type, String accessToken, String refreshToken, long accessTokenExpireTime) {
        this.type = type;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpireTime = accessTokenExpireTime;
    }
}
