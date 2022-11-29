package challenge.nDaysChallenge.dto.request;

import challenge.nDaysChallenge.domain.Member;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Data
@Getter
public class MemberSignUpDto {

    private Long number;

    @NotBlank(message = "id는 필수입력값입니다.")
    private String id;

    @NotEmpty(message = "pw를 입력해주세요.")
    private String pw;

    @NotBlank(message = "닉네임을 입력해주세요")
    private String nickname;

    @NotEmpty(message = "이미지를 골라주세요")
    private int image;

    private int roomLimit;

    @Builder
    public Member memberToEntity(){
        return Member.builder()
                .id(id)
                .nickname(nickname)
                .pw(pw)
                .image(image)
                .build();
    }

}
