package challenge.nDaysChallenge.dto.response.member;

import challenge.nDaysChallenge.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Constraint;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoResponseDto {

    private String id;

    private String nickname;

    private String pw;

    private int image;

    public static MemberInfoResponseDto of(Member member){
        return new MemberInfoResponseDto(member.getId(), member.getNickname(), member.getPw(), member.getImage());
    }

}
