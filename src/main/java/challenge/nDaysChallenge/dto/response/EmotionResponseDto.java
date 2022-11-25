package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Stickers;
import lombok.*;

@Getter
@NoArgsConstructor
public class EmotionResponseDto {

    private Long dajimNumber; //좋아요 등록한 다짐 넘버

    private Long memberNumber;//좋아요 등록한 멤버 넘버

    private String stickers; //선택한 감정스티커

    @Builder
    public EmotionResponseDto(Long dajimNumber, Long memberNumber, String stickers){
        this.dajimNumber=dajimNumber;
        this.memberNumber=memberNumber;
        this.stickers=stickers;
    }

}
