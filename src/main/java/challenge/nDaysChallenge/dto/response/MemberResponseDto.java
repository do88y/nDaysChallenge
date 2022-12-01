package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class MemberResponseDto {

    private String id;

    private String nickname;

    private int image;

    public static MemberResponseDto of(Member member){
        return new MemberResponseDto(member.getId(), member.getNickname(), member.getImage());
    }

}
