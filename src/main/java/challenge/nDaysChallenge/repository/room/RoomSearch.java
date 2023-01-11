package challenge.nDaysChallenge.repository.room;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomSearch {

    private String status;
    private String id;

    public RoomSearch(String status, String id) {
        this.status = status;
        this.id = id;
    }
}
