package challenge.nDaysChallenge.dto.response.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class DajimResponseDto {

    private long dajimNumber;

    private int image;

    private String nickname;

    private String content;

    private String open;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;

    //피드 다짐 리스트, 상세 챌린지 다짐에 필요한 정보
    public DajimResponseDto(long dajimNumber, String nickname, int image, String content, String open, LocalDateTime updatedDate){
        this.dajimNumber=dajimNumber;
        this.nickname=nickname;
        this.image=image;
        this.content=content;
        this.open=open;
        this.updatedDate=updatedDate;
    }

    public static DajimResponseDto of(Dajim dajim){
        return new DajimResponseDto(
                dajim.getNumber(),
                dajim.getMember().getNickname(),
                dajim.getMember().getImage(),
                dajim.getContent(),
                dajim.getOpen().toString(),
                dajim.getUpdatedDate()
        );
    }

}