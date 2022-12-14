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
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class QueryTest { //N+1 테스트

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

    @Test
    @DisplayName("멤버, 다짐, 이모션 등록")
    @BeforeEach
    public void 객체_세팅(){
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

        DajimUploadRequestDto dajimUploadRequestDto = new DajimUploadRequestDto("다짐 내용", "PRIVATE");

        //싱글룸 (룸1-멤버1)
        SingleRoom room1 = new SingleRoom("SingleRoom", new Period(LocalDate.now(), 10L), Category.ROUTINE, 2, "", 0, 0);
        roomRepository.save(room1);

        //싱글룸 다짐
        Dajim dajim = dajimRepository.save(Dajim.builder()
                .room(room1)
                .member(member1)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build());

        //그룹룸 (룸2-멤버1,2,3)
        GroupRoom room2 = new GroupRoom(member1, "GroupRoom", new Period(LocalDate.now(),100L), Category.ETC, 3, "", 0, 0);
        roomRepository.save(room2);
        RoomMember roomMember1 = RoomMember.createRoomMember(member1, room2, Stamp.createStamp(room2));
        RoomMember roomMember2 = RoomMember.createRoomMember(member2, room2, Stamp.createStamp(room2));
        RoomMember roomMember3 = RoomMember.createRoomMember(member3, room2, Stamp.createStamp(room2));
        roomMemberRepository.save(roomMember1);
        roomMemberRepository.save(roomMember2);
        roomMemberRepository.save(roomMember3);

        //그룹룸 다짐3
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

        //그룹룸 멤버2 다짐에 이모션 등록
        Emotion emotion = Emotion.builder()
                .member(member2)
                .dajim(dajim3)
                .sticker(Sticker.valueOf("TOUCHED"))
                .build();

        Emotion savedEmotion = emotionRepository.save(emotion);
        dajim3.addEmotions(savedEmotion);

    }

    @Test
    @DisplayName("다짐 내 이모션 조회")
    public void 다짐_내_이모션_조회(){
        Emotion emotionFound = emotionRepository.findByDajimAndMember(3L, 2L)
                .orElseThrow(()->new RuntimeException("이모션 객체를 찾을 수 없습니다."));

        Sticker stickers = emotionFound.getSticker();

        System.out.println(stickers.toString());
    }

    @Test
    @DisplayName("그룹 챌린지 내 다짐들 조회")
    public void 그룹_챌린지_다짐들_전체_조회(){
        List<Dajim> dajims = dajimRepository.findAllByRoomNumber(2L)
                .orElseThrow(()-> new RuntimeException("다짐을 확인할 수 없습니다."));

        assertThat(dajims.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("전체 피드 조회")
    public void 전체_피드_조회(){
        //given
        List<Long> groupRooms = new ArrayList<>();
        groupRooms.add(2L);

        //when
        List<Dajim> dajims = dajimFeedRepository.findAllByOpen();

        //then
        Assertions.assertThat(dajims.size()).isEqualTo(3);
        System.out.println(dajims.get(0).getNumber());
        System.out.println(dajims.get(1).getNumber());
        System.out.println(dajims.get(2).getNumber());

    }

    @Test
    @DisplayName("특정 멤버 조회")
    public void 멤버_조회(){
        Member member = memberRepository.findById("user@naver.com")
                .orElseThrow(()->new RuntimeException("회원을 찾을 수 없습니다."));

        assertThat(member.getNickname()).isEqualTo("userN");
    }

    @DisplayName("특정 룸넘버에 해당하는 룸 찾기")
    @Test
    void findByRoomNumber(){
        Room room = dajimRepository.findByRoomNumber(1L)
                .orElseThrow(()->new RuntimeException("룸을 찾을 수 없습니다."));

        assertThat(room.getName()).isEqualTo("SingleRoom");
    }

}