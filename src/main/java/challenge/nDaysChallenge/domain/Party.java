package challenge.nDaysChallenge.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Party {
    @Id @GeneratedValue
    private int member_number;

    private String member_nickname;

    private int room_number;
}
