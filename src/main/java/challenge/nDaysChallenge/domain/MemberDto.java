package challenge.nDaysChallenge.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@Getter
public class MemberDto {

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
    public MemberDto(Long number, String id, String pw,String nickname, int image, int roomLimit){
        this.number=number;
        this.id=id;
        this.pw=pw;
        this.nickname=nickname;
        this.image=image;
        this.roomLimit=roomLimit;
    }

}
