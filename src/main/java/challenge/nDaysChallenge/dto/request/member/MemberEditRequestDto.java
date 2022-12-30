package challenge.nDaysChallenge.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberEditRequestDto { //회원정보 수정 후 전달

    private String pw;

    private String nickname;

    private int image;

}
