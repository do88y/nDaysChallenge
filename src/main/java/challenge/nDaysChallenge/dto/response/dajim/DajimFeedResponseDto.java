package challenge.nDaysChallenge.dto.response.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Sticker;
import challenge.nDaysChallenge.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@Builder
@NoArgsConstructor
public class DajimFeedResponseDto {

    private long dajimNumber;

    private String nickname;

    private int image;

    private String content;

    //다짐별 모든 스티커
    private Map<String,Long> allStickers;

    //로그인 사용자의 다짐별 스티커
    @Nullable
    private String loginSticker;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedDate;

    public DajimFeedResponseDto(long dajimNumber, String nickname, int image, String content, Map<String,Long> allStickers, String loginSticker, LocalDateTime updatedDate) {
        this.dajimNumber = dajimNumber;
        this.nickname = nickname;
        this.image = image;
        this.content = content;
        this.allStickers = allStickers;
        this.loginSticker = loginSticker;
        this.updatedDate = updatedDate;
    }

    public static DajimFeedResponseDto of(Dajim dajim, @Nullable Member member){

        try {
            return new DajimFeedResponseDto(
                    dajim.getNumber(),
                    dajim.getMember().getNickname(),
                    dajim.getMember().getImage(),
                    dajim.getContent(),
                    dajim.getEmotions().stream()
                            .map(emotion -> emotion.getSticker().toString())
                            .collect(Collectors.groupingBy(s -> s, Collectors.counting())),
                    dajim.getEmotions().stream()
                            .filter(emotion -> emotion.getMember().getId().equals(member.getId()))
                            .map(emotion -> emotion.getSticker().toString())
                            .collect(Collectors.joining()),
                    dajim.getUpdatedDate()
            );
        } catch (NullPointerException e){
            return new DajimFeedResponseDto(
                    dajim.getNumber(),
                    dajim.getMember().getNickname(),
                    dajim.getMember().getImage(),
                    dajim.getContent(),
                    dajim.getEmotions().stream()
                            .map(emotion -> emotion.getSticker().toString())
                            .collect(Collectors.groupingBy(s -> s, Collectors.counting())),
                    "",
                    dajim.getUpdatedDate()
            );
        }

    }

}
