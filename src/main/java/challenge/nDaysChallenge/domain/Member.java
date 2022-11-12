package challenge.nDaysChallenge.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

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

    @Column(length = 6 ,nullable = false)
    private String nickname;

    @Column(length = 15, nullable = false)
    private String id;

    @Column(length = 15, nullable = false)
    private String pw;

    @Column(nullable = false)
    private int image;

    private int room_limit;  //챌린지 5개 제한

    //==비즈니스 로직==//
    }

