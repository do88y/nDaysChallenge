package challenge.nDaysChallenge.member;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.RoomMember;
import challenge.nDaysChallenge.dto.request.member.MemberEditRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.dto.response.member.MemberInfoResponseDto;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.service.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.ArrayList;
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

    @InjectMocks
    MemberService memberService;

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
        List<Dajim> dajims = new ArrayList<>();
        List<RoomMember> roomMembers = new ArrayList<>();

        //when
        doNothing().when(memberRepository).delete(member);
        when(dajimRepository.findAllByMemberNickname("aaa")).thenReturn(Optional.ofNullable(dajims));
        when(roomMemberRepository.findAllByMemberNickname("aaa")).thenReturn(Optional.ofNullable(roomMembers));
        String nickname = memberService.deleteMember(member);

        //then
        assertThat(nickname).isEqualTo(memberRequestDto.getNickname());
        verify(memberRepository, times(1)).delete(member);
    }

}
