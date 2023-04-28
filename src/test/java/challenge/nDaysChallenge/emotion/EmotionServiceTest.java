package challenge.nDaysChallenge.emotion;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.dajim.Sticker;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.Period;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import challenge.nDaysChallenge.dto.request.dajim.EmotionRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.dto.response.dajim.EmotionResponseDto;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.StampRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.SingleRoomRepository;
import challenge.nDaysChallenge.service.RoomService;
import challenge.nDaysChallenge.service.dajim.DajimService;
import challenge.nDaysChallenge.service.dajim.EmotionService;
import challenge.nDaysChallenge.service.member.MemberService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmotionServiceTest {

    @Mock
    private DajimRepository dajimRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomMemberRepository roomMemberRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private SingleRoomRepository singleRoomRepository;

    @Mock
    private StampRepository stampRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmotionRepository emotionRepository;

    @InjectMocks
    private DajimService dajimService;

    @InjectMocks
    private RoomService roomService;

    @InjectMocks
    private EmotionService emotionService;

    private MemberRequestDto memberRequestDto;
    private Member member;
    private Room room;
    private Dajim dajim;
    private Emotion emotion;

    @BeforeEach
    void 멤버_룸_다짐_세팅(){
        memberRequestDto = new MemberRequestDto("abc@naver.com","123","aaa",1);
        member = memberRequestDto.toMember(passwordEncoder);

        room = new SingleRoom(member.getNickname(), new Period(LocalDate.now(),30), Category.ROUTINE, 2, "보상");

        dajim = Dajim.builder()
                .number(1L)
                .member(member)
                .room(room)
                .open(Open.PUBLIC)
                .content("내용")
                .emotions(new ArrayList<>())
                .build();

    }

    @Test
    void 이모션_등록() {
        //given
        EmotionRequestDto emotionRequestDto = new EmotionRequestDto(dajim.getNumber(), "CHEER");

        //when
        when(dajimRepository.findByNumber(any())).thenReturn(Optional.of(dajim));
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        EmotionResponseDto emotionResponseDto = emotionService.uploadEmotion(emotionRequestDto, member.getId());

        //then
        verify(emotionRepository,times(1)).save(any());
        assertThat(emotionResponseDto.getSticker()).isEqualTo(emotionRequestDto.getSticker());
        assertThat(emotionResponseDto.getMemberNickname()).isEqualTo(member.getNickname());
    }

    @Test
    void 이모션_수정(){
        //given
        emotion = Emotion.builder()
                .sticker(Sticker.CHEER)
                .member(member)
                .dajim(dajim)
                .build();

        dajim.addEmotions(emotion);
        assertThat(dajim.getEmotions().get(0).getSticker().toString()).isEqualTo("CHEER");

        //when
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(emotionRepository.findByDajimAndMember(any(),any())).thenReturn(Optional.of(emotion));

        EmotionRequestDto emotionRequestDto = new EmotionRequestDto(dajim.getNumber(), "SURPRISE");
        EmotionResponseDto emotionResponseDto = emotionService.updateEmotion(emotionRequestDto, member.getId());

        //then
        assertThat(emotionResponseDto.getSticker()).isEqualTo("SURPRISE");
        assertThat(emotionResponseDto.getMemberNickname()).isEqualTo(member.getNickname());
        assertThat(dajim.getEmotions().get(0).getSticker().toString()).isEqualTo("SURPRISE");
        assertThat(dajim.getEmotions().size()).isEqualTo(1);
    }

    @Test
    void 이모션_삭제(){
        //given
        emotion = Emotion.builder()
                .sticker(Sticker.CHEER)
                .member(member)
                .dajim(dajim)
                .build();

        dajim.addEmotions(emotion);
        assertThat(dajim.getEmotions().size()).isEqualTo(1);

        EmotionRequestDto emotionRequestDto = new EmotionRequestDto(dajim.getNumber(), "WATCH");

        //when
        when(memberRepository.findById(member.getId())).thenReturn(Optional.of(member));
        when(dajimRepository.findByNumber(any())).thenReturn(Optional.of(dajim));
        when(emotionRepository.findByDajimAndMember(any(),any())).thenReturn(Optional.of(emotion));

        emotionService.deleteEmotion(emotionRequestDto, member.getId());

        //then
        verify(emotionRepository, times(1)).delete(any());
        assertThat(dajim.getEmotions().size()).isEqualTo(0);
    }

}
