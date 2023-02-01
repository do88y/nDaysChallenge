package challenge.nDaysChallenge.dto.request.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDto {

    private String id;

    private String pw;

    public LoginRequestDto(String id, String pw) {
        this.id = id;
        this.pw = pw;
    }

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(id, pw);
    }

}
