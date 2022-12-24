package challenge.nDaysChallenge.domain.dajim;

import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Dajim extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "dajim_number")
    private Long number;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "dajim", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Emotion> emotions = new ArrayList<>();

//    @OneToMany(mappedBy = "dajim", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<Report> reports = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "member_number")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_number")
    private Room room;

    private String content;

    //공개 여부 enum
    @Enumerated(EnumType.STRING)
    private Open open;

    @Builder
    public Dajim(Room room, Member member, String content, Open open) {
        this.room = room;
        this.member = member;
        this.content = content;
        this.open = open;
    }

    public Dajim update(Open open, String content) {
        this.open = open;
        this.content = content;
        return this;
    }

    public void addEmotions(Emotion emotion){
        this.emotions.add(emotion);
    }

    public void deleteEmotions(Emotion emotion){
        this.emotions.remove(emotion);
    }

}
