package challenge.nDaysChallenge.dajim;

import challenge.nDaysChallenge.config.SecurityConfig;
import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.dajim.Sticker;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import challenge.nDaysChallenge.dto.request.EmotionRequestDto;
import challenge.nDaysChallenge.dto.request.MemberRequestDto;
import challenge.nDaysChallenge.dto.response.EmotionResponseDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.dajim.DajimFeedRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmotionRepositoryTest {

    @Autowired
    DajimRepository dajimRepository;

    @Autowired
    DajimFeedRepository dajimFeedRepository;

    @Autowired
    EmotionRepository emotionRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomMemberRepository roomMemberRepository;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("이모션 등록")
    @Test
    @Transactional
    @Rollback(value = false)
    @BeforeEach
    void clickEmotion() {
        //given
        Member member1 = Member.builder()
                .id("newuser@naver.com")
                .pw("12345")
                .nickname("nickname")
                .image(1)
                .roomLimit(4)
                .authority(Authority.ROLE_USER)
                .build();

        SingleRoom room1 = new SingleRoom("newRoom", new Period(LocalDate.now(), 10L), Category.ROUTINE, 2, "", 0, 0);

        Dajim dajim = dajimRepository.save(Dajim.builder()
                .room(room1)
                .member(member1)
                .content("content")
                .open(Open.PUBLIC)
                .build());

        Sticker sticker = Sticker.valueOf("SURPRISE");

        Optional<Emotion> emotion = emotionRepository.findByDajimAndMember(dajim.getNumber(), member1.getNumber()).empty();

        //when
        Emotion uploadedEmotion = new Emotion();
        if (!emotion.isPresent()){
            uploadedEmotion = uploadEmotion(member1, dajim, sticker);
        }

        //then
        assertThat(emotion).isEmpty();
        assertThat(uploadedEmotion.getSticker().toString()).isEqualTo("SURPRISE");
        assertThat(dajim.getEmotions().get(0).getSticker().toString()).isEqualTo("SURPRISE");
        assertThat(dajim.getEmotions().size()).isEqualTo(1);
    }

    private Emotion uploadEmotion(Member member, Dajim dajim, Sticker sticker){ //이모션 등록
        Emotion emotion = Emotion.builder()
                .member(member)
                .dajim(dajim)
                .sticker(sticker)
                .build();

        emotionRepository.save(emotion);

        dajim.addEmotions(emotion); //다짐 엔티티 이모션리스트에 추가

        return emotion;
    }

    @DisplayName("이모션 변경")
    @Test
    @Transactional
    @Rollback(value = false)
    void updateEmotion(){
        //given
        Dajim dajim = dajimRepository.findByDajimNumber(1L)
                .orElseThrow(()->new RuntimeException("다짐을 찾을 수 없습니다"));

        System.out.println("다짐에 추가한 이모션 개수 : " + dajim.getEmotions().size());
        System.out.println("다짐에 추가한 이모션 스티커 내용 : " +
                dajim.getEmotions()
                        .stream().map(emotion -> emotion.getSticker().toString())
                        .collect(Collectors.toList())
        );

        Optional<Emotion> emotion = emotionRepository.findByDajimAndMember(1L, 1L);
        Emotion emotionFound = emotion.get();

        //when
        Emotion updatedEmotion = emotionFound.update(Sticker.CHEER); //수정

        System.out.println("다짐에 추가한 이모션 개수 : " + dajim.getEmotions().size());
        System.out.println("다짐에 추가한 이모션 스티커 내용 : " +
                dajim.getEmotions()
                        .stream().map(e -> e.getSticker().toString())
                        .collect(Collectors.toList())
        );

        //then
        assertThat(updatedEmotion.getSticker().toString()).isEqualTo("CHEER");
        assertThat(dajim.getEmotions().get(0)).isEqualTo(updatedEmotion);
        assertThat(dajim.getEmotions().size()).isEqualTo(1);
    }

    @DisplayName("이모션 삭제")
    @Test
    @Transactional
    @Rollback(value = false)
    void deleteEmotion(){
        //given
        Dajim dajim = dajimRepository.findByDajimNumber(1L)
                .orElseThrow(()->new RuntimeException("다짐을 찾을 수 없습니다"));

        System.out.println("다짐에 추가한 이모션 개수 : " + dajim.getEmotions().size());
        System.out.println("다짐에 추가한 이모션 스티커 내용 : " +
                dajim.getEmotions()
                        .stream().map(e -> e.getSticker().toString())
                        .collect(Collectors.toList())
        );

        Optional<Emotion> emotion = emotionRepository.findByDajimAndMember(1L, 1L);

        Emotion emotionFound = emotion.get();
        Sticker stickerBeforeUpdate = emotionFound.getSticker(); //업데이트 전 스티커
        LocalDateTime beforeUpdate = emotionFound.getUpdatedDate(); //업데이트 전 시간

        //when
        Emotion updatedEmotion = emotionFound.update(Sticker.SURPRISE); //똑같은 스티커 클릭

        LocalDateTime afterUpdate = updatedEmotion.getUpdatedDate(); //업데이트 후 시간
        if (beforeUpdate.isEqual(afterUpdate) && //업데이트 X & 똑같은 스티커 클릭 시
                stickerBeforeUpdate.toString().equals(updatedEmotion.getSticker().toString())){
            emotionRepository.delete(updatedEmotion);
            dajim.deleteEmotions(updatedEmotion);
        }

        System.out.println("다짐에 추가한 이모션 개수 : " + dajim.getEmotions().size());
        System.out.println("다짐에 추가한 이모션 스티커 내용 : " +
                dajim.getEmotions()
                        .stream().map(e -> e.getSticker().toString())
                        .collect(Collectors.toList())
        );

        //then
        assertThat(dajim.getEmotions()).isEmpty();

    }

}
