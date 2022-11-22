package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class DajimResponseDto {

    private Long dajimNumber;

    private String nickname;

    private String content;

    //피드 다짐 리스트, 상세 챌린지 다짐에 필요한 정보
    public DajimResponseDto(Long dajimNumber, String nickname, String content){
        this.dajimNumber=dajimNumber;
        this.nickname=nickname;
        this.content=content;
    }

}