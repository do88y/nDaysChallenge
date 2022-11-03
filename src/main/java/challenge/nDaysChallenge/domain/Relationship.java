package challenge.nDaysChallenge.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

@Entity
public class Relationship {

    @Id
    @GeneratedValue
    @Column(name = "relationship_number")
    private int number;
    private int user_number;
    private int friend_number;
    private Date date;
    private String status;

}
