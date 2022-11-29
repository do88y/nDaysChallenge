package challenge.nDaysChallenge.dto.request;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
<<<<<<< HEAD:src/main/java/challenge/nDaysChallenge/dto/request/SignupDto.java
import lombok.*;
=======
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
>>>>>>> cedc1d880e101d6df25ed2baaf8f9f2d210d442d:src/main/java/challenge/nDaysChallenge/dto/request/MemberRequestDto.java
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignupDto {

    private Long number;

    @NotBlank(message = "아이디는 필수 입력값입니다.")
    private String id;

<<<<<<< HEAD:src/main/java/challenge/nDaysChallenge/dto/request/SignupDto.java
    private String pw;

    private String nickname;

=======
    @NotEmpty(message = "비밀번호를 입력해주세요.")
    private String pw;

    @NotBlank(message = "닉네임을 입력해주세요.")
    private String nickname;

    @NotEmpty(message = "이미지를 선택해주세요.")
>>>>>>> cedc1d880e101d6df25ed2baaf8f9f2d210d442d:src/main/java/challenge/nDaysChallenge/dto/request/MemberRequestDto.java
    private int image;

    private int roomLimit;

    @Builder
    public Member toMember(PasswordEncoder passwordEncoder){
        return Member.builder()
                .id(id)
                .pw(passwordEncoder.encode(pw))
                .authority(Authority.ROLE_USER)
<<<<<<< HEAD:src/main/java/challenge/nDaysChallenge/dto/request/SignupDto.java
                .nickname(nickname)
=======
>>>>>>> cedc1d880e101d6df25ed2baaf8f9f2d210d442d:src/main/java/challenge/nDaysChallenge/dto/request/MemberRequestDto.java
                .image(image)
                .roomLimit(roomLimit)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication(){
        return new UsernamePasswordAuthenticationToken(id, pw);
    }

}
