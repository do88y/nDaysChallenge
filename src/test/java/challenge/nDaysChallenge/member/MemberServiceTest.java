package challenge.nDaysChallenge.member;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.member.MemberEditRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.dto.response.member.MemberInfoResponseDto;
import challenge.nDaysChallenge.dto.response.member.MemberResponseDto;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.StampRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.room.GroupRoomRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.SingleRoomRepository;
import challenge.nDaysChallenge.service.jwt.AuthService;
import challenge.nDaysChallenge.service.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    MemberRepository memberRepository;

    @Mock
    DajimRepository dajimRepository;

    @Mock
    RoomMemberRepository roomMemberRepository;

    @Mock
    StampRepository stampRepository;

    @Mock
    RoomRepository roomRepository;

    @Mock
    SingleRoomRepository singleRoomRepository;

    @Mock
    GroupRoomRepository groupRoomRepository;

    @InjectMocks
    MemberService memberService;

    @InjectMocks
    AuthService authService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Test
    void 아이디_중복_검사(){
        //when
        when(memberRepository.existsById(anyString())).thenReturn(true);
        String idCheck = memberService.idCheck("abc@naver.com");

        //then
        assertThat(idCheck).isEqualTo("exists");
    }

    @Test
    void 닉네임_중복_검사(){
        //when
        when(memberRepository.existsByNickname(anyString())).thenReturn(true);
        String nicknameCheck = memberService.nicknameCheck("abc");

        //then
        assertThat(nicknameCheck).isEqualTo("exists");
    }

    @Test
    void 회원정보_조회(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc@naver.com","123","aaa",1);
        Member member = memberRequestDto.toMember(passwordEncoder);

        //when
        when(memberRepository.findById("abc@naver.com")).thenReturn(Optional.ofNullable(member));
        MemberInfoResponseDto memberInfoResponseDto = memberService.findMemberInfo(member.getId());

        //then
        assertThat(memberInfoResponseDto.getNickname()).isEqualTo(memberRequestDto.getNickname());
        assertThat(memberInfoResponseDto.getImage()).isEqualTo(memberRequestDto.getImage());
    }

    @Test
    void 회원정보_수정(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc@naver.com","123","aaa",1);
        Member member = memberRequestDto.toMember(passwordEncoder);

        MemberEditRequestDto memberEditRequestDto = new MemberEditRequestDto("123", "abc", 4);

        //when
        MemberInfoResponseDto memberInfoResponseDto = memberService.editMemberInfo(member, memberEditRequestDto);

        //then
        assertThat(memberInfoResponseDto.getNickname()).isEqualTo(memberEditRequestDto.getNickname());
        assertThat(memberInfoResponseDto.getImage()).isEqualTo(memberEditRequestDto.getImage());
    }

    @Test
    void 회원_삭제(){
        //given
        //회원 세팅
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc@naver.com","123","aaa",1);
        Member member = memberRequestDto.toMember(passwordEncoder);

        SingleRoom room1 = new SingleRoom("기상", new Period(LocalDate.now(),30L), Category.ROUTINE, 2, "");
        SingleRoom room2 = new SingleRoom("공부", new Period(LocalDate.now(),30L), Category.ETC, 4, "");
        List<SingleRoom> singleRooms = Arrays.asList(room1, room2);
        when(singleRoomRepository.findAll(member)).thenReturn(singleRooms);
        when(roomMemberRepository.findAllByMemberNickname(member.getNickname())).thenReturn(Optional.of(new ArrayList<>()));
        when(groupRoomRepository.findAll(member)).thenReturn(new ArrayList<>());

        Dajim dajim1 = Dajim.builder()
                .number(1L)
                .room(room1)
                .member(member)
                .content("내용1")
                .open(Open.PUBLIC).build();

        Dajim dajim2 = Dajim.builder()
                .number(1L)
                .room(room2)
                .member(member)
                .content("내용2")
                .open(Open.PUBLIC).build();

        List<Dajim> dajims = Arrays.asList(dajim1, dajim2);
        when(dajimRepository.findAllByMemberNickname(member.getNickname())).thenReturn(Optional.of(dajims));

        //when
        doNothing().when(memberRepository).delete(member);
        String nickname = memberService.deleteMember(member);

        //then
        assertThat(nickname).isEqualTo(memberRequestDto.getNickname());
        verify(dajimRepository, times(1)).deleteAll(eq(dajims));
        verify(singleRoomRepository, times(1)).deleteAll(eq(singleRooms));
        verify(memberRepository, times(1)).delete(member);
    }

}
