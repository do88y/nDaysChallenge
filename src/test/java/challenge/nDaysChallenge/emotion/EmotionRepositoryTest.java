package challenge.nDaysChallenge.emotion;

import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Sticker;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.dajim.DajimUploadRequestDto;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EmotionRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    DajimRepository dajimRepository;

    @Autowired
    EmotionRepository emotionRepository;

    Member savedMember1, savedMember2;
    Room savedRoom;
    Dajim savedDajim;

    @BeforeEach
    void 멤버_룸_다짐_이모션_세팅(){
        Member member1 = Member.builder()
                .authority(Authority.ROLE_USER)
                .id("user@naver.com")
                .pw("1234")
                .nickname("nick")
                .image(3)
                .build();

        Member member2 = Member.builder()
                .authority(Authority.ROLE_USER)
                .id("user2@naver.com")
                .pw("1234")
                .nickname("nick2")
                .image(3)
                .build();

        savedMember1 = memberRepository.save(member1);
        savedMember2 = memberRepository.save(member2);

        Room room = new SingleRoom("기상", new Period(LocalDate.now(),30L) , Category.ROUTINE,2, "", 0, 0);

        savedRoom = roomRepository.save(room);

        Dajim dajim1 = DajimUploadRequestDto.toDajim(room, member1, "내용1", "PUBLIC");

        savedDajim = dajimRepository.save(dajim1);

        Emotion emotion1 = Emotion.builder()
                .member(member1)
                .dajim(dajim1)
                .sticker(Sticker.CHEER)
                .build();

        Emotion emotion2 = Emotion.builder()
                .member(member2)
                .dajim(dajim1)
                .sticker(Sticker.WATCH)
                .build();

        emotionRepository.save(emotion1);
        emotionRepository.save(emotion2);

        dajim1.addEmotions(emotion1);
        dajim1.addEmotions(emotion2);
    }

    @Test
    void 이모션_불러오기(){
        Emotion savedEmotion1 = emotionRepository.findByDajimAndMember(savedDajim.getNumber(), savedMember1.getNumber())
                .orElseThrow(()->new RuntimeException("이모션을 찾을 수 없습니다."));

        Emotion savedEmotion2 = emotionRepository.findByDajimAndMember(savedDajim.getNumber(), savedMember2.getNumber())
                .orElseThrow(()->new RuntimeException("이모션을 찾을 수 없습니다."));

        assertThat(savedEmotion1.getSticker().toString()).isEqualTo("CHEER");
        assertThat(savedEmotion2.getSticker().toString()).isEqualTo("WATCH");
    }

}
