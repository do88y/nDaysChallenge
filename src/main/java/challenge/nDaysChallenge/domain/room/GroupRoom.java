package challenge.nDaysChallenge.domain.room;

import challenge.nDaysChallenge.domain.RoomMember;
import lombok.Getter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("group")
@Getter
public class GroupRoom extends Room {

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<RoomMember> roomMemberList = new ArrayList<>();


    //==연관관계 메서드==//
    public void setGroupRoom(Room room) {
        this.room = room;
        room.getRoomMembers().add(this);
    }

    public void addRoomMember(RoomMember roomMember) {
        roomMemberList.add(roomMember);
        roomMember.setRoom(this);
    }

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
