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

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member user_number;

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member friend_number;


    private LocalDateTime date;

    //친구신청 상태//
    @Enumerated(EnumType.STRING)
    private Enum status;

}
