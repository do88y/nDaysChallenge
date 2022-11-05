package challenge.nDaysChallenge.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Room {
    @Id @GeneratedValue
    @Column(name = "room_number")
    private int number;

    @Column(name = "room_name")
    private String name;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String reward;

    @Enumerated(EnumType.STRING)
    private Category category;  //카테고리 [MINDFULNESS, EXERCISE, ROUTINE, ETC]

    private int successCount;

    @Enumerated(EnumType.STRING)
    private Room_status status;  //챌린지 상태 [CONTINUE, END]


}

