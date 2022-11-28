package challenge.nDaysChallenge.dto.request;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

    private Long number;

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String id;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String pw;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotEmpty(message = "이미지를 선택해주세요.")
    private int image;

    private int roomLimit;

    @Builder
    public Member toMember(PasswordEncoder passwordEncoder){
        return Member.builder()
                .id(id)
                .pw(passwordEncoder.encode(pw))
                .authority(Authority.ROLE_USER)
                .image(image)
                .roomLimit(roomLimit)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(id, pw);
    }

}
