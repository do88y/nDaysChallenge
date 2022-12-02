package challenge.nDaysChallenge.domain;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;
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
    private Member user;   //멤버 엔티티가 다 들어있음//

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL )
//    @JoinColumn(name = "friend_number")
    private Member friend;


    //친구신청 상태 enum으로 열거//
    @Enumerated(EnumType.STRING)
    private RelationshipStatus status;

    //친구 요청 날짜//
    private LocalDateTime requestedDate;

    //친구 수락 날짜//
    private LocalDateTime acceptedDate;


    //내가 수락한 친구들만 리스트에 들어가게//
    @OneToMany(mappedBy = "number")
    private  List<Member> friendsList = new ArrayList<>();


    //연관관계//
    public void addUser(Member user){
        this.user=user;
    }

    public void addFriend(Member friend){
        this.friend=friend;
    }


    public void addFriendList (Member user){
        this.friendsList.add(user);
    }




    //상태 업뎃 메서드//
    public Relationship update(RelationshipStatus status) {
        this.status=status;
        return this;
    }




    public static Relationship createRelationship( Member user, Member friend, LocalDateTime requestedDate, LocalDateTime acceptedDate){
        Relationship relationship = new Relationship();
        relationship.addUser(user);
        relationship.addFriend(friend);
        relationship.acceptedDate=acceptedDate;
        relationship.requestedDate=requestedDate;
        relationship.status=RelationshipStatus.REQUEST; //대기가 기본값이게//
        return relationship;
    }

}