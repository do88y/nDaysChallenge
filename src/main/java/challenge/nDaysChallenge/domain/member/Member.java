package challenge.nDaysChallenge.domain.member;

import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.room.RoomMember;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    //내가 수락한 친구들만 리스트에 들어가게//
    @OneToMany(mappedBy = "number", cascade = CascadeType.ALL, orphanRemoval = true)
    private  List<Relationship> confirmedFriendsList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RoomMember> roomMemberList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SingleRoom> singleRooms = new ArrayList<>();

    @Builder
    public Member(String id, String pw ,String nickname, int image, int roomLimit, Authority authority) {
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

    //친구 리스트에 추가 메서드//
    public void addFriendList (Relationship member){
        this.confirmedFriendsList.add(member);
    }


    public void addSingleRooms (SingleRoom singleRoom){
        this.singleRooms.add(singleRoom);
    }

}
