package challenge.nDaysChallenge.domain;

import lombok.CustomLog;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Dajim {

    @Id @GeneratedValue
    @Column(name = "dajim_number")
    private Long number;

    @ManyToOne
    @JoinColumn(name="room_number")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "member_number")
    private Member member;

    private String like;

    private String content;

    //공개 여부 enum
    private Open open;

}
