package challenge.nDaysChallenge.domain;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Relationship {

    @Id
    @GeneratedValue
    @Column(name = "relationship_number")
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    @JoinColumn
    @Column(name = "user_number")
    private Member user;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL )
    @JoinColumn
    @Column(name = "friend_number")
    private Member friend;


    //친구신청 상태 enum으로 열거//
    @Enumerated(EnumType.STRING)
    private RelationshipStatus status;


    //빌더는 값을 받아야 하는 것들만//
    @Builder
    public Relationship(Member userNumber, Member friendNumber){
        this.user=userNumber;
        this.friend=friendNumber;
        this.status=RelationshipStatus.REQUEST;
        //대기가 기본값이게//
    }



}
