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
import challenge.nDaysChallenge.dto.response.EmotionResponseDto;
import challenge.nDaysChallenge.repository.dajim.DajimFeedRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

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

    @DisplayName("피드 조회 - 다짐, 이모션")
    @Test
    @Transactional
    @Rollback(value = false)
    void viewFeed(){
        //given
//        Member member1 = Member.builder()
//                .id("user@naver.com")
//                .pw("12345")
//                .nickname("userN")
//                .image(1)
//                .roomLimit(4)
//                .authority(Authority.ROLE_USER)
//                .build();
//        Member member2 = Member.builder()
//                .id("user2@naver.com")
//                .pw("12345")
//                .nickname("userN2")
//                .image(1)
//                .roomLimit(4)
//                .authority(Authority.ROLE_USER)
//                .build();
//        Member member3 = Member.builder()
//                .id("user3@naver.com")
//                .pw("12345")
//                .nickname("userN3")
//                .image(1)
//                .roomLimit(4)
//                .authority(Authority.ROLE_USER)
//                .build();
//        SingleRoom.addRoomMember(member1);
//        Room room1 = Room.builder()
//                .name("newRoom")
//                .period(new Period(100L))
//                .category(Category.ROUTINE)
//                .passCount(5)
//                .build();
//        Room room2 = Room.builder()
//                .name("newRoom2")
//                .period(new Period(100L))
//                .category(Category.ROUTINE)
//                .passCount(5)
//                .build();
//        RoomMember.createRoomMember(member2,GroupRoom);
//        RoomMember.createRoomMember(member3,new GroupRoom());
//
//        DajimRequestDto dajimRequestDto = new DajimRequestDto("다짐 내용", Open.PUBLIC);
//
//        Dajim dajim = dajimRepository.save(Dajim.builder()
//                .room(room1)
//                .member(member1)
//                .content(dajimRequestDto.getContent())
//                .open(dajimRequestDto.getOpen())
//                .build());
//        Dajim dajim2 = dajimRepository.save(Dajim.builder()
//                .room(room1)
//                .member(member2)
//                .content(dajimRequestDto.getContent())
//                .open(dajimRequestDto.getOpen())
//                .build());
//        Dajim dajim3 = dajimRepository.save(Dajim.builder()
//                .room(room2)
//                .member(member2)
//                .content(dajimRequestDto.getContent())
//                .open(dajimRequestDto.getOpen())
//                .build());
//
//        //when
//        //멤버2이 가입된 룸
//        List<SingleRoom> singleRooms = member2.getSingleRooms();
//        List<Long> singleRoomNumbers = singleRooms.stream().map(singleRoom ->
//                        singleRoom.getNumber())
//                .collect(Collectors.toList());
//
//        List<RoomMember> roomMembers = member2.getRoomMemberList();
//        List<Long> GroupRoomNumbers = roomMembers.stream().map(roomMember ->
//                        roomMember.getRoom().getNumber())
//                .collect(Collectors.toList());
//
//        List<Dajim> dajims = dajimFeedRepository.findAllByMemberAndOpen(singleRoomNumbers, GroupRoomNumbers);
//
//        //then
//        assertThat(dajims.size()).isEqualTo(2);
    }

    @DisplayName("이모션 등록")
    @Test
    @Transactional
    @Rollback(value = false)
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
        Room room1 = Room.builder()
                .name("newRoom")
                .period(new Period(100L))
                .category(Category.ROUTINE)
                .passCount(5)
                .build();
        Dajim dajim = dajimRepository.save(Dajim.builder()
            .room(room1)
            .member(member1)
            .content("content")
            .open(Open.PUBLIC)
            .build());

        //when
        Dajim savedDajim = emotionRepository.findByDajimNumberForEmotion(dajim.getNumber());

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
    @Transactional
    @Rollback(value = false)
    void updateEmotion(){
        EmotionRequestDto requestDto = new EmotionRequestDto(1L,"SURPRISE");

        //when
        Emotion emotion = emotionRepository.findByEmotionNumber(1L, 1L);

        Emotion updatedEmotion;

        if (requestDto.getSticker()==null||requestDto.getSticker()==""){
            updatedEmotion = emotion.update(null);
        } else {
            updatedEmotion = emotion.update(Stickers.valueOf(requestDto.getSticker()));
        }

        EmotionResponseDto newEmotion = EmotionResponseDto.builder()
                        .dajimNumber(updatedEmotion.getDajim().getNumber())
                        .memberNumber(updatedEmotion.getMember().getNumber())
                        .stickers(updatedEmotion.getStickers().toString()).build();

        //then
        assertThat(newEmotion.getStickers()).isEqualTo("SURPRISE");
    }

    @DisplayName("이모션 삭제")
    @Test
    void deleteEmotion(){

    }

}
