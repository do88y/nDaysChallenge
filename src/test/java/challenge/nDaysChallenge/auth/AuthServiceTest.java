package challenge.nDaysChallenge.auth;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.dto.request.jwt.LoginRequestDto;
import challenge.nDaysChallenge.dto.request.jwt.TokenRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.dto.response.jwt.TokenResponseDto;
import challenge.nDaysChallenge.dto.response.member.MemberResponseDto;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.service.jwt.AuthService;
import challenge.nDaysChallenge.service.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @Test
    public void 회원가입(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc123@naver.com", "123", "닉네임", 4);

        //when
        MemberResponseDto memberResponseDto = authService.signup(memberRequestDto);

        //then
        assertThat(memberResponseDto.getId()).isEqualTo(memberRequestDto.getId());
        assertThat(memberResponseDto.getNickname()).isEqualTo(memberRequestDto.getNickname());
        assertThat(memberResponseDto.getImage()).isEqualTo(memberRequestDto.getImage());

        assertThat(memberService.nicknameCheck("닉네임")).isEqualTo("exists");
    }

    @Test
    public void 로그인(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc111@naver.com", "123", "피카츄", 4);
        authService.signup(memberRequestDto); //회원가입한 멤버 세팅

        LoginRequestDto loginRequestDto = new LoginRequestDto("abc111@naver.com", "123");

        //when
        TokenResponseDto tokenResponseDto = authService.login(loginRequestDto);
        System.out.println(tokenResponseDto);

        //then
        assertThat(tokenResponseDto.getType()).isNotEmpty();
        assertThat(tokenResponseDto.getAccessToken()).isNotEmpty();
        assertThat(tokenResponseDto.getRefreshToken()).isNotEmpty();
        assertThat(tokenResponseDto.getAccessTokenExpireTime()).isNotNull();
    }

    @Test
    @WithMockUser(value = "abc111@naver.com")
    public void 로그아웃(){
        //given
        LoginRequestDto loginRequestDto = new LoginRequestDto("abc111@naver.com", "123");
        authService.login(loginRequestDto); //로그인

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull(); //로그인 시 시큐리티 컨텍스트에 로그인 정보 존재
        assertThat(SecurityContextHolder.getContext().getAuthentication().getName()).isEqualTo("abc111@naver.com");

        //when
        authService.logout("abc111@naver.com");

        //then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull(); //로그아웃 시 시큐리티 컨텍스트에 로그인 정보 X
    }

}
