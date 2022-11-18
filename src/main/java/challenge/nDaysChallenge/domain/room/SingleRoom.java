package challenge.nDaysChallenge.domain.room;

import challenge.nDaysChallenge.domain.Member;
import lombok.Getter;

import javax.persistence.*;

@Entity
@DiscriminatorValue("single")
@Getter
public class SingleRoom extends Room {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;
}
