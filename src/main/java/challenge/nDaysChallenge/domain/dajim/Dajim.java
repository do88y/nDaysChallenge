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


    //dto 변환

    //챌린지 내 다짐 조회 dto로 변환
    public static List<DajimResponseDto> toDajimResponseDto(List<Dajim> dajims){
        List<DajimResponseDto> dajimsList = dajims
                .stream()
                .map(dajim -> DajimResponseDto.of(dajim))
                .collect(Collectors.toList());

        return dajimsList;
    }

    //피드 다짐 조회 dto (미로그인)
    public static List<DajimFeedResponseDto> toUnloggedInFeedDto(Slice<Dajim> dajimPage){ //다짐 슬라이스 -> 다짐피드 응답 dto 변환 (미로그인)
        List<DajimFeedResponseDto> dajimFeedList = dajimPage.getContent().stream()
                .map(dajim -> DajimFeedResponseDto.of(dajim, null))
                .collect(toList());

        return dajimFeedList;
    }

    //피드 다짐 조회 dto (로그인)
    public static List<DajimFeedResponseDto> toLoggedInFeedDto(Member member, Slice<Dajim> dajimPage) { //다짐 슬라이스 -> 다짐피드 응답 dto 변환 (로그인)
        List<DajimFeedResponseDto> dajimFeedList = dajimPage.getContent().stream()
                .map(dajim -> DajimFeedResponseDto.of(dajim, member))
                .collect(toList());

        return dajimFeedList;
    }

}
