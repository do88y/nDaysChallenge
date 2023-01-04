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
public class DajimUploadRequestDto {

    private String content;

    private String open;

    public DajimUploadRequestDto(String content, String open) {
        this.content = content;
        this.open = open;
    }

    public static Dajim toDajim(Room room, Member member, DajimUploadRequestDto dajimUploadRequestDto) {
        return Dajim.builder()
                .room(room)
                .member(member)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build();
    }

}
