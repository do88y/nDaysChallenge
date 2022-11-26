package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class DajimResponseDto {

    private Long dajimNumber;

    private String nickname;

    private String content;

    private String open;

    private LocalDateTime updatedDate;

    //피드 다짐 리스트, 상세 챌린지 다짐에 필요한 정보
    public DajimResponseDto(Long dajimNumber, String nickname, String content, String open, LocalDateTime updatedDate){
        this.dajimNumber=dajimNumber;
        this.nickname=nickname;
        this.content=content;
        this.open=open;
        this.updatedDate=updatedDate;
    }

}