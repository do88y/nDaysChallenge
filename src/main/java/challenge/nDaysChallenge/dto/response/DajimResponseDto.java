package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class DajimResponseDto {

    private long dajimNumber;

    private int image;

    private String nickname;

    private String content;

    private String open;

    @JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    private LocalDateTime updatedDate;

    //피드 다짐 리스트, 상세 챌린지 다짐에 필요한 정보
    @Builder
    public DajimResponseDto(long dajimNumber, String nickname, int image, String content, String open, LocalDateTime updatedDate){
        this.dajimNumber=dajimNumber;
        this.nickname=nickname;
        this.image=image;
        this.content=content;
        this.open=open;
        this.updatedDate=updatedDate;
    }

}