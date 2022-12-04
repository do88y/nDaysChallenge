package challenge.nDaysChallenge.dajim;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.dajim.Stickers;
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
import challenge.nDaysChallenge.service.RoomService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.as;
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

    @DisplayName("피드 조회 - 다짐, 이모션")
    @Test
    @Transactional
    @Rollback(value = false)
    void viewFeed(){
        //given
        //멤버3 룸2
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

        DajimRequestDto dajimRequestDto = new DajimRequestDto(null,"다짐 내용", "PRIVATE");

        //싱글룸 (룸1-멤버1)
        SingleRoom room1 = new SingleRoom("SingleRoom", new Period(10L), Category.ROUTINE, 2, "");
        roomRepository.save(room1);

        //싱글룸 다짐
        Dajim dajim = dajimRepository.save(Dajim.builder()
                .room(room1)
                .member(member1)
                .content(dajimRequestDto.getContent())
                .open(Open.valueOf(dajimRequestDto.getOpen()))
                .build());

        //그룹룸 (룸2-멤버1,2,3)
        GroupRoom room2 = new GroupRoom("GroupRoom", new Period(100L), Category.ETC, 3, "");
        roomRepository.save(room2);
        RoomMember roomMember1 = RoomMember.createRoomMember(member1, room2);
        RoomMember roomMember2 = RoomMember.createRoomMember(member2, room2);
        RoomMember roomMember3 = RoomMember.createRoomMember(member3, room2);
        roomMemberRepository.save(roomMember1);
        roomMemberRepository.save(roomMember2);
        roomMemberRepository.save(roomMember3);

        //그룹룸 다짐3
        Dajim dajim2 = dajimRepository.save(Dajim.builder()
                .room(room2)
                .member(member1)
                .content(dajimRequestDto.getContent())
                .open(Open.valueOf(dajimRequestDto.getOpen()))
                .build());
        Dajim dajim3 = dajimRepository.save(Dajim.builder()
                .room(room2)
                .member(member2)
                .content(dajimRequestDto.getContent())
                .open(Open.valueOf(dajimRequestDto.getOpen()))
                .build());
        Dajim dajim4 = dajimRepository.save(Dajim.builder()
                .room(room2)
                .member(member3)
                .content(dajimRequestDto.getContent())
                .open(Open.valueOf("PRIVATE"))
                .build());

        //그룹룸 멤버2 다짐에 이모션 등록
        Emotion emotion = Emotion.builder()
                .member(member2)
                .dajim(dajim3)
                .stickers(Stickers.valueOf("TOUCHED"))
                .build();

        Emotion savedEmotion = emotionRepository.save(emotion);
        dajim3.addEmotions(savedEmotion);

        //이모션 불러오기ㅜ
        List<Emotion> emotions = dajim3.getEmotions();
        List<String> stickersList = emotions.stream().map(emotion1 ->
                        emotion1.getStickers().toString())
                .collect(Collectors.toList());

        //when
        //멤버2 싱글룸 불러오기
        List<SingleRoom> singleRooms = member1.getSingleRooms();
        List<Long> singleRoomNumbers = singleRooms.stream().map(singleRoom ->
                        singleRoom.getNumber())
                .collect(Collectors.toList());

        //멤버2 그룹룸 불러오기
        List<RoomMember> roomMemberList = member1.getRoomMemberList();
        List<Long> groupRoomNumbers = roomMemberList.stream().map(roomMember ->
                        roomMember.getRoom().getNumber())
                .collect(Collectors.toList());

        //해당 룸넘버들의 다짐 불러오기
        List<Dajim> dajims = dajimFeedRepository.findAllByMemberAndOpen(groupRoomNumbers, singleRoomNumbers);

        //then
        for (Dajim c : dajims){
            System.out.println("다짐 넘버 리스트 : "+c.getNumber());
        }

        //멤버2
//        assertThat(singleRooms.size()).isEqualTo(1); //싱글룸 0개
//        assertThat(roomMemberList.size()).isEqualTo(1); //그룹룸 1개
//        assertThat(dajims.size()).isEqualTo(4); //해당 그룹룸에 다짐 3개
//        assertThat(dajim3.getEmotions().get(0).getStickers().toString()).isEqualTo("TOUCHED");
//        assertThat(stickersList.get(0)).isEqualTo("TOUCHED");
    }

    @DisplayName("이모션 등록")
    @Test
    void clickEmotion(){
        //given
        Member member1 = Member.builder()
                .id("user@naver.com")
                .pw("12345")
                .nickname("userN")
                .image(1)
                .roomLimit(4)
                .authority(Authority.ROLE_USER)
                .build();

        SingleRoom room1 = new SingleRoom("newRoom", new Period(10L), Category.ROUTINE, 2, "");

        Dajim dajim = dajimRepository.save(Dajim.builder()
            .room(room1)
            .member(member1)
            .content("content")
            .open(Open.PUBLIC)
            .build());

        //when
        Dajim savedDajim = emotionRepository.findByDajimNumberForEmotion(dajim.getNumber())
                .orElseThrow(()->new RuntimeException("다짐을 찾을 수 없습니다."));

        Stickers sticker = Stickers.valueOf("SURPRISE");

        Emotion emotion = Emotion.builder()
                .member(member1)
                .dajim(savedDajim)
                .stickers(sticker)
                .build();

        Emotion savedEmotion = emotionRepository.save(emotion);

        //then
        assertThat(savedEmotion.getStickers().toString()).isEqualTo("SURPRISE");
    }

    @DisplayName("이모션 변경 및 삭제")
    @Test
    void updateEmotion(){
        //given
        Member member1 = Member.builder()
                .id("user@naver.com")
                .pw("12345")
                .nickname("userN")
                .image(1)
                .roomLimit(4)
                .authority(Authority.ROLE_USER)
                .build();
        SingleRoom room1 = new SingleRoom("newRoom", new Period(100L), Category.ROUTINE, 5, "");
        Dajim dajim = dajimRepository.save(Dajim.builder()
                .room(room1)
                .member(member1)
                .content("content")
                .open(Open.PUBLIC)
                .build());
        Emotion emotion = Emotion.builder()
                .member(member1)
                .dajim(dajim)
                .stickers(Stickers.valueOf("CHEER"))
                .build();
        emotionRepository.save(emotion);

        EmotionRequestDto requestDto = new EmotionRequestDto(1L,"SURPRISE");

        //when
        Emotion updatedEmotion;

        if (requestDto.getSticker()==null||requestDto.getSticker().equals("")){
            updatedEmotion = emotion.update(null);
        } else {
            updatedEmotion = emotion.update(Stickers.valueOf(requestDto.getSticker()));
        }

        EmotionResponseDto newEmotion = EmotionResponseDto.builder()
                        .dajimNumber(updatedEmotion.getDajim().getNumber())
                        .memberNickname(updatedEmotion.getMember().getNickname())
                        .stickers(updatedEmotion.getStickers().toString()).build();

        //then
        assertThat(newEmotion.getStickers()).isEqualTo("SURPRISE");
    }

}
