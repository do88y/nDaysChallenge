package challenge.nDaysChallenge.domain.dajim;

import challenge.nDaysChallenge.domain.Member;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Emotion extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "emotion_number")
    private Long number;

    @Enumerated(EnumType.STRING)
    private Sticker sticker; //감정스티커

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dajim_number")
    private Dajim dajim;

    @Builder
    public Emotion(Member member, Dajim dajim, Sticker sticker) {
        this.member = member;
        this.dajim = dajim;
        this.sticker = sticker;
    }

    public Emotion update(Sticker sticker) {
        this.sticker=sticker;
        return this;
    }

}
