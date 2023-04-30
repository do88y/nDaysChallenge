package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.member.Authority;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminLoginDto {

    private String id;
    private Authority auth;

    @Builder
    public AdminLoginDto(String id, Authority auth) {
        this.id = id;
        this.auth = auth;
    }
}
