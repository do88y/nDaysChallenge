package challenge.nDaysChallenge.domain.room;

import challenge.nDaysChallenge.domain.RoomMember;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupRoom extends Room {

    @OneToMany(mappedBy = "member")
    private List<RoomMember> roomMemberList = new ArrayList<>();


    //==비즈니스 로직==//

    /**
     * 챌린지 삭제
     */
    public void delete() {
        for (RoomMember roomMember : roomMemberList) {
            roomMember.delete();
        }
    }

}
