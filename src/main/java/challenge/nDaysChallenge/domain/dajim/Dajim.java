package challenge.nDaysChallenge.domain.dajim;

<<<<<<< HEAD
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.Report;
import challenge.nDaysChallenge.domain.RoomMember;
import lombok.NoArgsConstructor;
=======
import challenge.nDaysChallenge.domain.*;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Persistable;
>>>>>>> refs/remotes/origin/develop

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
<<<<<<< HEAD
@NoArgsConstructor
public class Dajim {
=======
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Dajim extends BaseEntity implements Persistable<Long> {
>>>>>>> refs/remotes/origin/develop

    @Id @GeneratedValue
    @Column(name = "dajim_number")
    private Long number;

<<<<<<< HEAD
    //다중 @OneToMany 로 인한 N+1 오류는 BatchSize + Join Fetch 로 해결할 예정
    @OneToMany(mappedBy = "dajim", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Likes> likes = new ArrayList<>();

    @OneToMany(mappedBy = "dajim", cascade = CascadeType.ALL, orphanRemoval = true)
    List<DajimComment> comments = new ArrayList<>();
=======
    @OneToMany(mappedBy = "dajim", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Emotion> emotions = new ArrayList<>();
>>>>>>> refs/remotes/origin/develop

    @OneToMany(mappedBy = "dajim", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Report> reports = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
<<<<<<< HEAD
    @JoinColumn(name = "room_member_number")
    private RoomMember roomMember;
=======
    @JoinColumn(name = "room_number")
    private Room room;
>>>>>>> refs/remotes/origin/develop

    private String content;

    //공개 여부 enum
    @Enumerated(EnumType.STRING)
    private Open open;

<<<<<<< HEAD
=======
    public Dajim(Room room, Member member, String content){
        this.room=room;
        this.member=member;
        this.content=content;
    }

    @Override
    public Long getId() {
        return number;
    }

    @Override
    public boolean isNew() {
        return getCreatedDate()==null;
    }

>>>>>>> refs/remotes/origin/develop
}
