package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Stickers;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class DajimFeedResponseDto {

    private Long dajimNumber;

    private String nickname;

    private String content;

    private List<Emotion> emotionsList;

    public DajimFeedResponseDto(Long dajimNumber, String nickname, String content, List<Emotion> emotionsList) {
        this.dajimNumber = dajimNumber;
        this.nickname = nickname;
        this.content = content;
        this.emotionsList = emotionsList;
    }

}
