package challenge.nDaysChallenge.domain;

import challenge.nDaysChallenge.domain.room.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Getter
public class Stamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stamp_number")
    public Long number;

    @OneToOne(fetch = FetchType.LAZY)
    public Room room;
    public String day;


    //생성 메서드
    public static Stamp createStamp(Room room) {
        Stamp stamp = new Stamp();
        stamp.room = room;
        stamp.day = "";
        return stamp;
    }

    //스탬프 찍기
    public Stamp updateStamp(Room room, String day) {
        this.room = room;

        if (this.day.equals("")) {
            this.day = day;
        } else {
            this.day += "," + day;
        }

        return this;
    }
}
