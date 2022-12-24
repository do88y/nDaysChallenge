package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Sticker;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class DajimFeedResponseDto {

    private long dajimNumber;

    private String nickname;

    private String content;

    private List<String> stickersList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;

    public DajimFeedResponseDto(long dajimNumber, String nickname, String content, List<String> stickersList, LocalDateTime updatedDate) {
        this.dajimNumber = dajimNumber;
        this.nickname = nickname;
        this.content = content;
        this.stickersList = stickersList;
        this.updatedDate = updatedDate;
    }

}
