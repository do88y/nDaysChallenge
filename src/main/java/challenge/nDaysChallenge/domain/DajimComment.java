package challenge.nDaysChallenge.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
public class Dajim_comment {

    @Id @GeneratedValue
    @Column(name = "comment_number")
    private Long number;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<Report> reports = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dajim_number")
    private Dajim dajim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    private String content;

}
