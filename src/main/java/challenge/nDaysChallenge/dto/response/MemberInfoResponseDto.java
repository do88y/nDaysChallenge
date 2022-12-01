package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberInfoResponseDto {

    private String id;

    private String nickname;

    private String pw;

    private int image;

    public static MemberInfoResponseDto of(Member member){
        return new MemberInfoResponseDto(member.getId(), member.getNickname(), member.getPw(), member.getImage());
    }

}
