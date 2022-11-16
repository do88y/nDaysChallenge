package challenge.nDaysChallenge.domain.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.Report;
import challenge.nDaysChallenge.domain.RoomMember;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Dajim {

    @Id @GeneratedValue
    @Column(name = "dajim_number")
    private Long number;

    //다중 @OneToMany 로 인한 N+1 오류는 BatchSize + Join Fetch 로 해결할 예정
    @OneToMany(mappedBy = "dajim", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "dajim", cascade = CascadeType.ALL, orphanRemoval = true)
    List<DajimComment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "dajim", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Report> reports = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_member_number")
    private RoomMember roomMember;

    private String content;

    //공개 여부 enum
    @Enumerated(EnumType.STRING)
    private Open open;

}
