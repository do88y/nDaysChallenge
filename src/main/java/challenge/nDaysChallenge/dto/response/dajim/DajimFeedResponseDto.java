package challenge.nDaysChallenge.dto.response.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Sticker;
import challenge.nDaysChallenge.dto.response.member.MemberResponseDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
public class DajimFeedResponseDto {

    private long dajimNumber;

    private String nickname;

    private int image;

    private String content;

    private List<String> stickersList;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;

    public DajimFeedResponseDto(long dajimNumber, String nickname, int image, String content, List<String> stickersList, LocalDateTime updatedDate) {
        this.dajimNumber = dajimNumber;
        this.nickname = nickname;
        this.image = image;
        this.content = content;
        this.stickersList = stickersList;
        this.updatedDate = updatedDate;
    }

    public static DajimFeedResponseDto of(Dajim dajim){
        return new DajimFeedResponseDto(
                dajim.getNumber(),
                dajim.getMember().getNickname(),
                dajim.getMember().getImage(),
                dajim.getContent(),
                dajim.getEmotions().stream().map(emotion ->
                                emotion.getSticker().toString())
                        .collect(Collectors.toList()),
                dajim.getUpdatedDate()
                );
    }

}
