package challenge.nDaysChallenge.dto.request.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.room.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DajimUpdateRequestDto {

    private Long dajimNumber;

    private String nickname;

    private String content;

    private String open;

    public DajimUpdateRequestDto(Long dajimNumber, String content, String open) {
        this.dajimNumber = dajimNumber;
        this.nickname = getNickname();
        this.content = content;
        this.open = open;
    }

}
