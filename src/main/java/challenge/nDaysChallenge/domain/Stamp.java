package challenge.nDaysChallenge.domain;

import challenge.nDaysChallenge.domain.room.Room;
import lombok.AllArgsConstructor;
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
    public int day1;
    public int day2;
    public int day3;
    public int day4;
    public int day5;
    public int day6;
    public int day7;
    public int day8;
    public int day9;
    public int day10;
    public int day11;
    public int day12;
    public int day13;
    public int day14;
    public int day15;
    public int day16;
    public int day17;
    public int day18;
    public int day19;
    public int day20;
    public int day21;
    public int day22;
    public int day23;
    public int day24;
    public int day25;
    public int day26;
    public int day27;
    public int day28;
    public int day29;
    public int day30;


    //생성메서드
    public static Stamp createStamp(Room room) {
        Stamp stamp = new Stamp();
        stamp.room = room;
        return stamp;
    }

    public Stamp updateStamp(Room room, int day1, int day2, int day3, int day4, int day5, int day6, int day7, int day8, int day9, int day10, int day11, int day12, int day13, int day14, int day15, int day16, int day17, int day18, int day19, int day20, int day21, int day22, int day23, int day24, int day25, int day26, int day27, int day28, int day29, int day30) {
        this.room = room;
        this.day1 = day1;
        this.day2 = day2;
        this.day3 = day3;
        this.day4 = day4;
        this.day5 = day5;
        this.day6 = day6;
        this.day7 = day7;
        this.day8 = day8;
        this.day9 = day9;
        this.day10 = day10;
        this.day11 = day11;
        this.day12 = day12;
        this.day13 = day13;
        this.day14 = day14;
        this.day15 = day15;
        this.day16 = day16;
        this.day17 = day17;
        this.day18 = day18;
        this.day19 = day19;
        this.day20 = day20;
        this.day21 = day21;
        this.day22 = day22;
        this.day23 = day23;
        this.day24 = day24;
        this.day25 = day25;
        this.day26 = day26;
        this.day27 = day27;
        this.day28 = day28;
        this.day29 = day29;
        this.day30 = day30;

        return this;
    }
}
