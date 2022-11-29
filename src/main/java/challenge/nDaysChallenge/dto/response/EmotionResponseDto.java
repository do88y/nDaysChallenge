package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Stickers;
import lombok.*;

@Getter
@NoArgsConstructor
public class EmotionResponseDto {

    private Long dajimNumber; //좋아요 등록한 다짐 넘버

    private String memberNickname;//좋아요 등록한 멤버 닉네임

    private String stickers; //선택한 감정스티커

    @Builder
    public EmotionResponseDto(Long dajimNumber, String memberNickname, String stickers){
        this.dajimNumber=dajimNumber;
        this.memberNickname=memberNickname;
        this.stickers=stickers;
    }

}
