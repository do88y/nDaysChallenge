package challenge.nDaysChallenge.domain.dajim;

import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.dto.response.dajim.DajimFeedResponseDto;
import challenge.nDaysChallenge.dto.response.dajim.DajimResponseDto;
import lombok.*;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Dajim extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "dajim_number")
    private Long number;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "dajim", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    List<Emotion> emotions = new ArrayList<>();

//    @OneToMany(mappedBy = "dajim", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<Report> reports = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_number")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_number")
    private Room room;

    private String content;

    //공개 여부 enum
    @Enumerated(EnumType.STRING)
    private Open open;

    public Dajim update(Open open, String content) {
        this.open = open;
        this.content = content;
        this.emotions.clear();
        return this;
    }

    public void addEmotions(Emotion emotion){
        this.emotions.add(emotion);
    }

    public void deleteEmotions(Emotion emotion){
        this.emotions.remove(emotion);
    }

}
