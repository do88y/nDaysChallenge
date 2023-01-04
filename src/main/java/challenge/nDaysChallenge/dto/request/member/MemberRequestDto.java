package challenge.nDaysChallenge.dto.request.member;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class MemberRequestDto {

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String id;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    private String pw;

    @NotBlank(message = "닉네임을 입력해 주세요.")
    private String nickname;

    @NotBlank(message = "프로필 사진을 선택해 주세요.")
    private int image;

    private int roomLimit;

    public MemberRequestDto(String id, String pw, String nickname, int image, int roomLimit) {
        this.id = id;
        this.pw = pw;
        this.nickname = nickname;
        this.image = image;
        this.roomLimit = roomLimit;
    }

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

}
