package challenge.nDaysChallenge.dajim;

import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.RoomMember;
import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.dajim.Sticker;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.dajim.DajimUploadRequestDto;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.dajim.DajimFeedRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DajimFeedRepositoryTest {

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

    @DisplayName("ํผ๋ ์กฐํ - ๋ค์ง, ์ด๋ชจ์")
    @Test
    @Transactional
    @Rollback(value = false)
    void viewFeed(){
        //given
        //๋ฉค๋ฒ3 ๋ฃธ2
        Member member1 = Member.builder()
                .id("user@naver.com")
                .pw("12345")
                .nickname("userN")
                .image(1)
                .roomLimit(4)
                .authority(Authority.ROLE_USER)
                .build();
        Member member2 = Member.builder()
                .id("user2@naver.com")
                .pw("12345")
                .nickname("userN2")
                .image(1)
                .roomLimit(4)
                .authority(Authority.ROLE_USER)
                .build();
        Member member3 = Member.builder()
                .id("user3@naver.com")
                .pw("12345")
                .nickname("userN3")
                .image(1)
                .roomLimit(4)
                .authority(Authority.ROLE_USER)
                .build();

        DajimUploadRequestDto dajimUploadRequestDto = new DajimUploadRequestDto("๋ค์ง ๋ด์ฉ", "PUBLIC");

        //์ฑ๊ธ๋ฃธ (๋ฃธ1-๋ฉค๋ฒ1)
        SingleRoom room1 = new SingleRoom("SingleRoom", new Period(LocalDate.now(), 10L), Category.ROUTINE, 2, "", 0, 0);
        member1.addSingleRooms(room1);
        roomRepository.save(room1);

        //์ฑ๊ธ๋ฃธ ๋ค์ง
        Dajim dajim = dajimRepository.save(Dajim.builder()
                .room(room1)
                .member(member1)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build());

        //๊ทธ๋ฃน๋ฃธ (๋ฃธ2-๋ฉค๋ฒ1,2,3)
        GroupRoom room2 = new GroupRoom(member1, "GroupRoom", new Period(LocalDate.now(),100L), Category.ETC, 3, "", 0, 0);
        roomRepository.save(room2);
        RoomMember roomMember1 = RoomMember.createRoomMember(member1, room2, Stamp.createStamp(room2));
        RoomMember roomMember2 = RoomMember.createRoomMember(member2, room2, Stamp.createStamp(room2));
        RoomMember roomMember3 = RoomMember.createRoomMember(member3, room2, Stamp.createStamp(room2));
        roomMemberRepository.save(roomMember1);
        roomMemberRepository.save(roomMember2);
        roomMemberRepository.save(roomMember3);

        //๊ทธ๋ฃน๋ฃธ ๋ค์ง3
        Dajim dajim2 = dajimRepository.save(Dajim.builder()
                .room(room2)
                .member(member1)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build());
        Dajim dajim3 = dajimRepository.save(Dajim.builder()
                .room(room2)
                .member(member2)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build());
        Dajim dajim4 = dajimRepository.save(Dajim.builder()
                .room(room2)
                .member(member3)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf("PRIVATE"))
                .build());

        //๊ทธ๋ฃน๋ฃธ ๋ฉค๋ฒ2 ๋ค์ง์ ์ด๋ชจ์ ๋ฑ๋ก
        Emotion emotion = Emotion.builder()
                .member(member2)
                .dajim(dajim3)
                .sticker(Sticker.valueOf("TOUCHED"))
                .build();

        Emotion savedEmotion = emotionRepository.save(emotion);
        dajim3.addEmotions(savedEmotion);

        //์ด๋ชจ์ ๋ถ๋ฌ์ค๊ธฐใ
        List<Emotion> emotions = dajim3.getEmotions();
        List<String> stickersList = emotions.stream().map(emotion1 ->
                        emotion1.getSticker().toString())
                .collect(Collectors.toList());

        //ํด๋น ๋ฃธ๋๋ฒ๋ค์ ๋ค์ง ๋ถ๋ฌ์ค๊ธฐ
        List<Dajim> dajims = dajimFeedRepository.findAllByOpen();

        //then
        for (Dajim c : dajims){
            System.out.println("๋ค์ง ๋๋ฒ ๋ฆฌ์คํธ : "+c.getNumber());
        }

        //๋ฉค๋ฒ2
        assertThat(dajims.size()).isEqualTo(3); //๋ค์ง 4๊ฐ
        assertThat(dajim3.getEmotions().get(0).getSticker().toString()).isEqualTo("TOUCHED");
        assertThat(stickersList.get(0)).isEqualTo("TOUCHED");
    }

}
