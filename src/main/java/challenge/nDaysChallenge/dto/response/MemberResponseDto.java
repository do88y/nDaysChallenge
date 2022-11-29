package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {

    private String id;

    public static MemberResponseDto of(Member member){
        return new MemberResponseDto(member.getId());
    }

}
