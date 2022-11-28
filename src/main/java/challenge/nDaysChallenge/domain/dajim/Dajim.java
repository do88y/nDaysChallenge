package challenge.nDaysChallenge.domain.dajim;

import challenge.nDaysChallenge.domain.BaseEntity;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Persistable;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@DynamicUpdate
public class Dajim extends BaseEntity implements Persistable<Long> {

    @Id @GeneratedValue
    @Column(name = "dajim_number")
    private Long number;

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
    public Dajim(Room room, Member member, String content, Open open){
        this.room=room;
        this.member=member;
        this.content=content;
        this.open=open;
    }

    @Override
    public Long getId() {
        return number;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate()==null;
    }

    public Dajim update(Open open, String content) {
        this.open = open;
        this.content = content;
        return this;
    }

}