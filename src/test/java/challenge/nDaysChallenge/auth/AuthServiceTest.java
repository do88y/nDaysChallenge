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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @After
    public void 세팅한_회원_삭제(){
        memberService.deleteMember("testest@naver.com");
    }

    @Test
    public void 회원가입(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("testest@naver.com", "123", "fortest", 4);

        //when
        MemberResponseDto memberResponseDto = authService.signup(memberRequestDto);

        //then
        assertThat(memberResponseDto.getId()).isEqualTo(memberRequestDto.getId());
        assertThat(memberResponseDto.getNickname()).isEqualTo(memberRequestDto.getNickname());
        assertThat(memberResponseDto.getImage()).isEqualTo(memberRequestDto.getImage());
    }

    @Test
    public void 로그인(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("testest@naver.com", "123", "fortest", 4);
        MemberResponseDto memberResponseDto = authService.signup(memberRequestDto);

        LoginRequestDto loginRequestDto = new LoginRequestDto("testest@naver.com", "123");

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
    public void 로그아웃(){
        //given
        MemberRequestDto memberRequestDto = new MemberRequestDto("testest@naver.com", "123", "fortest", 4);
        authService.signup(memberRequestDto);

        LoginRequestDto loginRequestDto = new LoginRequestDto("testest@naver.com", "123");
        authService.login(loginRequestDto); //로그인

        //when
        authService.logout("testest@naver.com");

        //then
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull(); //로그아웃 시 시큐리티 컨텍스트에 로그인 정보 X
    }

}
