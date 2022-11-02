package challenge.nDaysChallenge.domain;


import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@NoArgsConstructor
public class Room {
    @Id@GeneratedValue
    @Column(name = "room_number")
    private int number;

    @Column(name = "room_name")
    private String name;

    private Date startDate;

    private Date endDate;

    private String reward;

    private int category;

    private int successCount;

    private String status;


}

