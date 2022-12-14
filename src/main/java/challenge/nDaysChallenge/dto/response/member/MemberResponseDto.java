package challenge.nDaysChallenge.dto.response.member;

import challenge.nDaysChallenge.domain.member.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {

    private String id;

    private String nickname;

    private int image;

    public static MemberResponseDto of(Member member){
        return new MemberResponseDto(member.getId(), member.getNickname(), member.getImage());
    }

}
