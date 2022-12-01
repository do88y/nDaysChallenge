package challenge.nDaysChallenge.dto.request;

import lombok.Getter;

@Getter
public class MemberEditRequestDto { //회원정보 수정 후 전달

    private String pw;

    private String nickname;

    private int image;

}
