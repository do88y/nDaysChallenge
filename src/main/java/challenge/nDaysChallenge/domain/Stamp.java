package challenge.nDaysChallenge.domain;

import challenge.nDaysChallenge.domain.room.Room;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDate;

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
    public LocalDate latestDate;


    //==생성 메서드==//
    public static Stamp createStamp(Room room) {
        Stamp stamp = new Stamp();
        stamp.room = room;
        stamp.day = "";
        stamp.latestDate = LocalDate.now().minusDays(1L);
        return stamp;
    }


    //==비즈니스 로직==//
    //스탬프 찍기
    public Stamp updateStamp(Room room, String day) {

        if (!this.latestDate.isEqual(LocalDate.now())) {
            this.room = room;
            this.latestDate = LocalDate.now();

            if (this.day.equals("")) {  //첫 날
                this.day = day;
            } else {
                this.day += "," + day;
            }
        } else {
            System.out.println("스탬프를 중복 요청했습니다.");
        }

        return this;
    }

    //마지막 스탬프
    public String getLatestStamp() {

        if (this.day.length() > 1) {
            String[] stampArr = this.day.split(",");
            return stampArr[stampArr.length-1];
        }
        return this.day;
    }
}
