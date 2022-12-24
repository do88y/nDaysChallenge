package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Sticker;
import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Null;

@Getter
@NoArgsConstructor
public class EmotionResponseDto {

    private Long dajimNumber; //좋아요 등록한 다짐 넘버

    private String memberNickname;//좋아요 등록한 멤버 닉네임

    private String sticker; //선택한 감정스티커

    public EmotionResponseDto(long dajimNumber, String memberNickname, String sticker){
        this.dajimNumber = dajimNumber;
        this.memberNickname = memberNickname;
        this.sticker = sticker;
    }

    //삭제할 이모션객체 다짐넘버 null로 수정
    public void updateNull(){
        this.dajimNumber = null;
    }

}
