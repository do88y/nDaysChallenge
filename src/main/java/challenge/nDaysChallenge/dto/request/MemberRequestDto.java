package challenge.nDaysChallenge.dto.request;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class MemberRequestDto {

    @NotBlank(message = "아이디를 입력해주세요.")
    private String id;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String pw;

    private String nickname;
    private int image;

    private int roomLimit;

    public MemberRequestDto(String id, String pw, String nickname, int image, int roomLimit) {
        this.id = id;
        this.pw = pw;
        this.nickname = nickname;
        this.image = image;
        this.roomLimit = roomLimit;
    }

    @Builder
    public Member toMember(PasswordEncoder passwordEncoder){
        return Member.builder()
                .id(id)
                .pw(passwordEncoder.encode(pw))
                .authority(Authority.ROLE_USER)
                .nickname(nickname)
                .image(image)
                .roomLimit(roomLimit)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(id, pw);
    }

}
