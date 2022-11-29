package challenge.nDaysChallenge.domain;

import challenge.nDaysChallenge.domain.room.Room;
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


    @Column(name = "member_id", length = 15, nullable = false)
    @Email(message = "이메일 형식으로 입력해주세요.")
    private String id;


    //내가 수락한 친구들만 리스트에 들어가게//
    @OneToMany(mappedBy = "friend")
    private List<Relationship> friendsList = new ArrayList<Relationship>();


    @Column(length = 150, nullable = false)
    private String pw;

    @Column(unique = true, length = 6, nullable = false)
    private String nickname;

    @Column(nullable = false)
    private int image;

    private int roomLimit;  //챌린지 5개 제한

    @Enumerated(EnumType.STRING)
    private Authority authority;


    @OneToMany(mappedBy = "member")
    private List<RoomMember> roomMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "number")
    private List<Room> singleRooms = new ArrayList<>();

    @Builder
    public Member(String id, String pw, String nickname, int image, int roomLimit, Authority authority) {
        this.id = id;
        this.pw = pw;
        this.nickname = nickname;
        this.image = image;
        this.roomLimit=roomLimit;
        this.authority = authority;
    }

    //챌린지 갯수 조회
    public int countRooms() {
        this.roomLimit = this.getRoomMemberList().size() + this.getSingleRooms().size();

        return roomLimit;
    }
}
