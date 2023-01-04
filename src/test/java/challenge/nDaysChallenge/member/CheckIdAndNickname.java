package challenge.nDaysChallenge.member;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.service.jwt.AuthService;
import challenge.nDaysChallenge.service.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CheckIdAndNickname {

    @InjectMocks
    AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Mock
    MemberRepository memberRepository;

    @BeforeTransaction
    @Transactional
    public void 회원가입(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc@naver.com","123","aaa",1,2);
        Member member = memberRequestDto.toMember(passwordEncoder);
        given(memberRepository.save(any())).willReturn(member);
    }

    @Test
    void 아이디_중복_검사(){
        //mocking
        given(memberRepository.existsById("abc@naver.com")).willReturn(true);
        given(memberRepository.existsById("abcd@naver.com")).willReturn(false);

        //when
        String id = authService.idCheck("abc@naver.com");
        String id2 = authService.idCheck("abcd@naver.com");

        //then
        assertThat(id).isEqualTo("exists");
        assertThat(id2).isEqualTo("ok");

    }

    @Test
    void 닉네임_중복_검사(){
        //mocking
        given(memberRepository.existsByNickname("aaa")).willReturn(true);
        given(memberRepository.existsByNickname("aaab")).willReturn(false);

        //when
        String nickname = authService.nicknameCheck("aaa");
        String nickname2 = authService.nicknameCheck("aaab");

        //then
        assertThat(nickname).isEqualTo("exists");
        assertThat(nickname2).isEqualTo("ok");
    }

}
