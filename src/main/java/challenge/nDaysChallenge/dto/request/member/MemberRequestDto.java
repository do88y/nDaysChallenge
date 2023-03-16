package challenge.nDaysChallenge.dto.request.member;

import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.member.Member;
import lombok.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
public class MemberRequestDto {

    @NotEmpty(message = "아이디를 입력해 주세요.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String id;

    @NotEmpty(message = "비밀번호를 입력해 주세요.")
    private String pw;

    @NotEmpty(message = "닉네임을 입력해 주세요.")
    private String nickname;

    @NotNull(message = "프로필 사진을 선택해 주세요.")
    @Min(value = 1)
    @Max(value = 6)
    private int image;

    public MemberRequestDto(String id, String pw, String nickname, int image) {
        this.id = id;
        this.pw = pw;
        this.nickname = nickname;
        this.image = image;
    }

    public Member toMember(PasswordEncoder passwordEncoder){
        return Member.builder()
                .id(id)
                .pw(passwordEncoder.encode(pw))
                .authority(Authority.ROLE_USER)
                .nickname(nickname)
                .image(image)
                .build();
    }

}
