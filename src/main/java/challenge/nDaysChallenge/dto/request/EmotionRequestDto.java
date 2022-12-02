package challenge.nDaysChallenge.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class EmotionRequestDto {

    Long dajimNumber; //다짐 번호

    String sticker; //감정스티커 받아오기

    public EmotionRequestDto(Long dajimNumber, String sticker) {
        this.dajimNumber = dajimNumber;
        this.sticker = sticker;
    }
}