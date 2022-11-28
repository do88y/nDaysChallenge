package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Stickers;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class DajimFeedResponseDto {

    private Long dajimNumber;

    private String nickname;

    private String content;

    private List<String> stickersList;

    private LocalDateTime updatedDate;

    public DajimFeedResponseDto(Long dajimNumber, String nickname, String content, List<String> stickersList, LocalDateTime updatedDate) {
        this.dajimNumber = dajimNumber;
        this.nickname = nickname;
        this.content = content;
        this.stickersList = stickersList;
        this.updatedDate = updatedDate;
    }

}
