package challenge.nDaysChallenge.domain;

import javax.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;

@Entity
public class Relationship {

    @Id
    @GeneratedValue
    @Column(name = "relationship_number")
    private Long number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member user_number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member friend_number;


    private LocalDateTime date;

    //친구신청 상태 enum으로 열거//
    @Enumerated(EnumType.STRING)
    private Enum status;

}
