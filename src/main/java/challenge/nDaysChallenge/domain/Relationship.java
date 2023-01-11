package challenge.nDaysChallenge.domain;

import challenge.nDaysChallenge.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;



@Entity
@Getter
@BatchSize(size = 100)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public  class Relationship {

    @Id
    @GeneratedValue
    @Column(name = "relationship_number")
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY ,cascade = CascadeType.ALL )
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


    //연관관계//
    public void addUser(Member user){
        this.user=user;
    }

    public void addFriend(Member friend){
        this.friend=friend;
    }



    //상태 업뎃 메서드//
    public Relationship updateStatus(RelationshipStatus status) {
        this.status=status;
        return this;
    }

    //relationship 에 user 와 friend 의 기본 베이스 정보//
    public static Relationship readyCreateRelation( Member user, Member friend){
        Relationship relationship = new Relationship();
        relationship.addUser(user);
        relationship.addFriend(friend);
        relationship.requestedDate=LocalDateTime.now();
        relationship.status=RelationshipStatus.REQUEST; //대기가 기본값이게//
        return relationship;
    }

}