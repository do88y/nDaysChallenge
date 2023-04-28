package challenge.nDaysChallenge.auth;

import challenge.nDaysChallenge.NDaysChallengeApplication;
import challenge.nDaysChallenge.controller.AuthController;
import challenge.nDaysChallenge.dto.request.jwt.LoginRequestDto;
import challenge.nDaysChallenge.dto.request.jwt.TokenRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.repository.jwt.RefreshTokenRepository;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.service.jwt.AuthService;
import challenge.nDaysChallenge.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NDaysChallengeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @BeforeTransaction
    public void 회원가입() {
        MemberRequestDto memberRequestDto = new MemberRequestDto("test@naver.com", "123", "nickkk", 4);

        authService.signup(memberRequestDto);
    }

    @AfterTransaction
    public void 회원삭제(){
        memberService.deleteMember("test@naver.com");
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "test@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void 로그인() throws Exception{
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@naver.com","123");

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        ).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "test@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void 로그아웃() throws Exception{
        //로그인
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@naver.com","123");

        MvcResult result = mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        ).andExpect(status().isOk()).andReturn();

        //로그아웃
        mockMvc.perform(
                post("/auth/logout")
        ).andExpect(status().isMovedPermanently());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "test@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void 토큰_재발급() throws Exception{
        //로그인 -> 액세스, 리프레쉬 토큰 확인
        LoginRequestDto loginRequestDto = new LoginRequestDto("test@naver.com","123");

        MvcResult result = mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        ).andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString();
        String accessToken = response.substring(response.indexOf("accessToken")+14, response.indexOf("refreshToken")-3);
        String refreshToken = response.substring(response.indexOf("refreshToken")+15, response.indexOf("accessTokenExpireTime")-3);

        //액세스토큰 재발급
        TokenRequestDto tokenRequestDto = new TokenRequestDto(accessToken, refreshToken);

        MvcResult newAccessToken = mockMvc.perform(
                post("/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRequestDto))
                        .header("Authorization", "Bearer " + accessToken)
        ).andExpect(status().isOk()).andReturn();

        System.out.println("기존 액세스, 리프레시 토큰 : " + response);
        System.out.println("재발급한 액세스, 리프레시 토큰 : " + newAccessToken.getResponse().getContentAsString());
    }

}
