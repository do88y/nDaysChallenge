package challenge.nDaysChallenge.domain;

import challenge.nDaysChallenge.domain.member.Member;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    public Member member;
    public String day;
    public LocalDate latestDate;
    protected int usedPassCount;
    protected int successCount;


    //==생성 메서드==//
    public static Stamp createStamp(Room room, Member member) {
        Stamp stamp = new Stamp();
        stamp.room = room;
        stamp.member = member;
        stamp.day = "";
        stamp.latestDate = LocalDate.now().minusDays(1L);
        stamp.usedPassCount = 0;
        stamp.successCount = 0;
        return stamp;
    }


    //==비즈니스 로직==//
    //스탬프 찍기
    public Stamp updateStamp(Room room, String day) {

            this.room = room;
            this.latestDate = LocalDate.now();

            if (this.day.equals("")) {  //첫 날
                this.day = day;
            } else {
                this.day += day;
            }

        return this;
    }

    //마지막 스탬프 찾기
    public String getLatestStamp() {

        if (day.length() > 1) {
            return day.substring(day.length()-1);
        }
        return day;
    }

    //성공 +1
    public void addSuccess() {
        successCount += 1;
    }

    //실패 +1
    public void addPass() {
        usedPassCount += 1;
    }
}
