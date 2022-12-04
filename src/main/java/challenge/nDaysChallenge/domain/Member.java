package challenge.nDaysChallenge.domain;

import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_number")
    private Long number;

    @Column(name = "member_id")
    private String id;

    private String pw;

    private String nickname;

    private int image;

    private int roomLimit;  //챌린지 5개 제한

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @OneToMany(mappedBy = "member")
    private final List<RoomMember> roomMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "number")
    private final List<SingleRoom> singleRooms = new ArrayList<>();

    @Builder
    public Member(String id, String pw, String nickname, int image, int roomLimit, Authority authority) {
        this.id = id;
        this.pw = pw;
        this.nickname = nickname;
        this.image = image;
        this.roomLimit=roomLimit;
        this.authority = authority;
    }

    public Member update (String nickname, String pw, int image){
        this.nickname = nickname;
        this.pw = pw;
        this.image = image;
        return this;
    }


/*    //챌린지 갯수 조회
    public int countRooms() {
        this.roomLimit = this.getRoomMemberList().size() + this.getSingleRooms().size();

        return roomLimit;
    }*/
}
