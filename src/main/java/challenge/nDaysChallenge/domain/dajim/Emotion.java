package challenge.nDaysChallenge.domain.dajim;

import challenge.nDaysChallenge.domain.Member;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Emotion extends BaseEntity implements Persistable<Long> {

    @Id @GeneratedValue
    @Column(name = "emotion_number")
    private Long number;

    @Enumerated(EnumType.STRING)
    private Stickers stickers; //감정스티커

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_number")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "dajim_number")
    private Dajim dajim;

    @Builder
    public Emotion(Member member, Dajim dajim, Stickers stickers) {
        this.member = member;
        this.dajim = dajim;
        this.stickers = stickers;
    }

    @Override
    public Long getId() {
        return number;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate()==null;
    }

    public Emotion update(Stickers stickers) {
        this.stickers=stickers;
        return this;
    }

}
