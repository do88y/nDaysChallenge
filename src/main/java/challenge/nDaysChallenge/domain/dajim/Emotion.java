package challenge.nDaysChallenge.domain.dajim;

import challenge.nDaysChallenge.domain.BaseEntity;
import challenge.nDaysChallenge.domain.Member;
import lombok.*;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Emotion extends BaseEntity implements Persistable<Long> {

    @Id @GeneratedValue
    @Column(name = "emotion_number")
    private Long number;

    @Enumerated(EnumType.STRING)
    private Stickers stickers; //감정스티커

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dajim_number")
    private Dajim dajim;

    @Override
    public Long getId() {
        return number;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate()==null;
    }
}
