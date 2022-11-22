package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Stickers;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmotionResponseDto {

    Long dajimNumber; //좋아요 등록한 다짐 넘버

    Long memberNumber;//좋아요 등록한 멤버 넘버

    Stickers stickers; //선택한 감정스티커

    public EmotionResponseDto(Long dajimNumber, Long memberNumber, String stickers){
        this.dajimNumber=dajimNumber;
        this.memberNumber=memberNumber;
        this.stickers=Stickers.valueOf(stickers);
    }

}
