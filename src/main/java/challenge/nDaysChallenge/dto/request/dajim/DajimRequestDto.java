package challenge.nDaysChallenge.dto.request.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.room.Room;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DajimRequestDto {

    private Long dajimNumber;

    private String content;

    private String open;

    public DajimRequestDto(Long dajimNumber, String content, String open) {
        this.dajimNumber = dajimNumber;
        this.content = content;
        this.open = open;
    }

    public Dajim toDajim(Room room, Member member, DajimRequestDto dajimRequestDto) {
        return Dajim.builder()
                .room(room)
                .member(member)
                .content(dajimRequestDto.getContent())
                .open(Open.valueOf(dajimRequestDto.getOpen()))
                .build();
    }

}
