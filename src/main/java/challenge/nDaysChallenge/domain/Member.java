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
import java.util.Optional;


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

    @OneToMany(mappedBy = "friendNumber")
    private List<Relationship> friends = new ArrayList<>();

    @Column(length = 6 ,nullable = false)

    private String id;

    @Column(length = 15, nullable = false)
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

    @OneToMany(mappedBy = "member")
    private List<SingleRoom> singleRooms = new ArrayList<>();

    //==친구목록==//
    @OneToMany(mappedBy = "friendNumber")
    private List<Relationship> friends = new ArrayList<>();


    @Builder
    public Member(Long number, String id, String pw, String nickname, int image, int roomLimit, Authority authority) {
        this.number = number;
        this.id = id;
        this.pw = pw;
        this.nickname = nickname;
        this.image = image;
        this.roomLimit=roomLimit;
        this.authority = authority;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.pw= passwordEncoder.encode(pw);
    }
    public Authority authority() {
        return authority;
    }
}

