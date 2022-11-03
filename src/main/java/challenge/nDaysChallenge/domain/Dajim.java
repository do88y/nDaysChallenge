package challenge.nDaysChallenge.domain;

import lombok.CustomLog;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.beans.ConstructorProperties;

@Entity
@NoArgsConstructor
public class Dajim {

    @Id @GeneratedValue
    @Column(name = "dajim_number")
    private int number;

    private int room_number;

    private int member_number;

    private String dajim_like;

    private String dajim_content;

}
