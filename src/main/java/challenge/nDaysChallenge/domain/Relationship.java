package challenge.nDaysChallenge.domain;

import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Stickers;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public  class Relationship {

    @Id
    @GeneratedValue
    @Column(name = "relationship_number")
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_number")
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL )
//    @JoinColumn(name = "friend_number")
    private Member friend;

    //내가 수락한 친구들만 리스트에 들어가게//
    @OneToMany(mappedBy = "number")
    private  List<Member> friendsList = new ArrayList<>();

    //친구신청 상태 enum으로 열거//
    @Enumerated(EnumType.STRING)
    private RelationshipStatus status;




    //연관관계//
    public void addFriendList (Member friend){
        this.friendsList.add(friend);
    }

    //상태 업뎃//
    public Relationship update(RelationshipStatus status) {
        this.status=status;
        return this;
    }


    //빌더는 값을 받아야 하는 것들만//
    @Builder
    public Relationship(Long number, Member userNumber, Member friendNumber){
        this.number=number;
        this.user=userNumber;
        this.friend=friendNumber;
        this.status=RelationshipStatus.REQUEST;
        //대기가 기본값이게//
    }


}