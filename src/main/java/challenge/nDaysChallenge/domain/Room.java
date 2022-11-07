package challenge.nDaysChallenge.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_number")
    private Long number;

    @Column(name = "room_name", nullable = false, length = 30)
    private String name;

    @Column(nullable = false)
    private LocalDateTime start_date;

    @Column(nullable = false)
    private LocalDateTime end_date;

    @Column(length = 50)
    private String reward;

    @Enumerated(EnumType.STRING)
    private Category category;  //카테고리 [MINDFULNESS, EXERCISE, ROUTINE, ETC]

    private int success_count;

    @Enumerated(EnumType.STRING)
    private Room_status status;  //챌린지 상태 [CONTINUE, END]


}

