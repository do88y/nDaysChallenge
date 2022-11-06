package challenge.nDaysChallenge.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_number")
    private Long number;

    private String nickname;

    private String id;

    private String pw;

    private int image;

    private int room_limit;

    //==비즈니스 로직==//
    }

