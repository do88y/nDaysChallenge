package challenge.nDaysChallenge.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.dajim.Sticker;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.dajim.DajimUpdateRequestDto;
import challenge.nDaysChallenge.dto.request.dajim.DajimUploadRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.dto.response.dajim.DajimFeedResponseDto;
import challenge.nDaysChallenge.dto.response.dajim.DajimResponseDto;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.StampRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.SingleRoomRepository;
import challenge.nDaysChallenge.service.RoomService;
import challenge.nDaysChallenge.service.dajim.CustomSliceImpl;
import challenge.nDaysChallenge.service.dajim.DajimFeedService;
import challenge.nDaysChallenge.service.dajim.DajimService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Array;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DajimServiceTest {

    @Mock
    private DajimRepository dajimRepository;

    @Mock
    private EmotionRepository emotionRepository;

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

    @InjectMocks
    private DajimService dajimService;

    @InjectMocks
    private DajimFeedService dajimFeedService;

    @InjectMocks
    private RoomService roomService;

    private MemberRequestDto memberRequestDto;
    private Member member;
    private Room room, room2;
    private Dajim dajim, dajim2;

    @BeforeEach
    void 멤버_룸_다짐_세팅(){
        memberRequestDto = new MemberRequestDto("abc@naver.com","123","aaa",1);
        member = memberRequestDto.toMember(passwordEncoder);

        room = new SingleRoom(member.getNickname(), new Period(LocalDate.now(),30L), Category.ROUTINE, 2, "보상");
        room2 = new SingleRoom(member.getNickname(), new Period(LocalDate.now(),30L), Category.ROUTINE, 2, "보상");

        dajim = Dajim.builder()
                .number(1L)
                .member(member)
                .room(room)
                .open(Open.PUBLIC)
                .content("내용")
                .emotions(new ArrayList<>())
                .build();

        dajim2 = Dajim.builder()
                .number(2L)
                .member(member)
                .room(room2)
                .open(Open.PUBLIC)
                .content("내용2")
                .emotions(new ArrayList<>())
                .build();

        Emotion emotion = Emotion.builder()
                .dajim(dajim)
                .sticker(Sticker.CHEER)
                .member(member)
                .build();

        Emotion emotion2 = Emotion.builder()
                .dajim(dajim)
                .sticker(Sticker.LIKE)
                .member(member)
                .build();

        dajim.addEmotions(emotion);
        dajim2.addEmotions(emotion2);
    }

    @Test
    void 다짐_등록(){
        //given
        DajimUploadRequestDto dajimUploadRequestDto = new DajimUploadRequestDto("내용", "PUBLIC");

        //when
        when(roomRepository.findByNumber(any())).thenReturn(Optional.of(room));
        when(dajimRepository.save(any())).thenReturn(dajim);

        DajimResponseDto dajimResponseDto = dajimService.uploadDajim(room.getNumber(), dajimUploadRequestDto, member);

        //then
        assertThat(dajimResponseDto.getContent()).isEqualTo(dajimUploadRequestDto.getContent());
        assertThat(dajimResponseDto.getNickname()).isEqualTo(memberRequestDto.getNickname());
    }

    @Test
    void 다짐_수정(){
        //given
        DajimUpdateRequestDto dajimUpdateRequestDto = new DajimUpdateRequestDto(dajim.getNumber(), "수정한 내용", "PRIVATE");

        //when
        when(dajimRepository.findByMemberNumberAndRoomNumber(any(),any())).thenReturn(Optional.ofNullable(dajim));
        DajimResponseDto dajimResponseDto = dajimService.updateDajim(room.getNumber(), dajimUpdateRequestDto, member);

        //then
        assertThat(dajimResponseDto.getContent()).isEqualTo(dajimUpdateRequestDto.getContent());
        assertThat(dajimResponseDto.getOpen()).isEqualTo(dajimUpdateRequestDto.getOpen());
    }

    @Test
    void 챌린지룸_다짐들_조회(){
        //given
        List<Dajim> dajims = new ArrayList<>();
        dajims.add(dajim);
        dajims.add(dajim2);
        when(dajimRepository.findAllByRoomNumber(any())).thenReturn(Optional.of(dajims));

        //when
        List<DajimResponseDto> dajimResponseDtos = dajimService.viewDajimInRoom(room.getNumber());

        //then
        System.out.println(dajimResponseDtos.stream().map(d->d.getContent()).collect(Collectors.toList()));
        assertThat(dajimResponseDtos.size()).isEqualTo(2);
    }

    @Test
    void 미로그인_피드_다짐들_조회() {
        //given
        List<Dajim> dajims = Arrays.asList(dajim, dajim2); //피드 다짐 리스트
        Slice<Dajim> dajimPage = new CustomSliceImpl<>(dajims, Pageable.ofSize(10).withPage(0), false); //피드 다짐 페이징 객체(슬라이스)

        //when
        when(dajimRepository.findByOpen(Open.PUBLIC, Pageable.ofSize(10).withPage(0))).thenReturn(dajimPage);
        Slice<DajimFeedResponseDto> dajimFeedPage = dajimFeedService.viewFeedWithoutLogin(Pageable.ofSize(10).withPage(0));

        //then
        assertThat(dajimFeedPage.getContent().size()).isEqualTo(2);
        System.out.println(dajimFeedPage.stream().map(d->d.getContent()).collect(Collectors.toList()));
    }

    @Test
    void 로그인유저_피드_다짐들_조회() {
        //given
        List<Dajim> dajims = Arrays.asList(dajim, dajim2); //피드 다짐 리스트
        Slice<Dajim> dajimPage = new CustomSliceImpl<>(dajims, Pageable.ofSize(10).withPage(0), false); //피드 다짐 페이징 객체(슬라이스)

        //when
        when(dajimRepository.findByOpen(Open.PUBLIC, Pageable.ofSize(10).withPage(0))).thenReturn(dajimPage);
        Slice<DajimFeedResponseDto> dajimFeedPage = dajimFeedService.viewFeedLoggedIn(member, Pageable.ofSize(10).withPage(0));

        //then
        assertThat(dajimFeedPage.getContent().size()).isEqualTo(2);
        System.out.println(dajimFeedPage.stream().map(d->d.getContent()).collect(Collectors.toList()));
    }

}
