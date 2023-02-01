package challenge.nDaysChallenge.auth;

import challenge.nDaysChallenge.NDaysChallengeApplication;
import challenge.nDaysChallenge.controller.AuthController;
import challenge.nDaysChallenge.dto.request.jwt.LoginRequestDto;
import challenge.nDaysChallenge.dto.request.jwt.TokenRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.service.jwt.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.TransactionManager;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    private MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc123@naver.com", "123", "닉네임", 4);

        mockMvc.perform(
                post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto))
        ).andExpect(status().isOk());
    }

    @Test
    public void 로그인() throws Exception{
        //회원가입
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc1@naver.com", "123", "닉네임2", 4);

        mockMvc.perform(
                post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto))
        ).andExpect(status().isOk());

        //로그인
        LoginRequestDto loginRequestDto = new LoginRequestDto("abc1@naver.com","123");

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        ).andExpect(status().isOk());
    }

    @Test
    public void 로그아웃() throws Exception{
        //회원가입
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc12@naver.com", "123", "닉네임3", 4);

        mockMvc.perform(
                post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto))
        ).andExpect(status().isOk());

        //로그인
        LoginRequestDto loginRequestDto = new LoginRequestDto("abc12@naver.com","123");

        MvcResult result = mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        ).andExpect(status().isOk()).andReturn();

        //로그아웃
        String response = result.getResponse().getContentAsString(); //로그인 시 받은 Responsebody
        String accessToken = response.substring(response.indexOf("accessToken")+14, response.indexOf("refreshToken")-3); //중 AccessToken 값만 요청 헤더에 넣기

        System.out.println(accessToken);
        mockMvc.perform(
                post("/auth/logout")
                        .header("Authorization", "Bearer "+accessToken) //설정한 Authorization 형식
        ).andExpect(status().isOk());
    }

    @Test
    public void 토큰_재발급() throws Exception{
        //회원가입
        MemberRequestDto memberRequestDto = new MemberRequestDto("abcd12@naver.com", "123", "닉네임4", 4);

        mockMvc.perform(
                post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberRequestDto))
        ).andExpect(status().isOk());

        //로그인
        LoginRequestDto loginRequestDto = new LoginRequestDto("abcd12@naver.com","123");

        MvcResult result = mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        ).andExpect(status().isOk()).andReturn();

        //액세스토큰 재발급
        String response = result.getResponse().getContentAsString();
        String accessToken = response.substring(response.indexOf("accessToken")+14, response.indexOf("refreshToken")-3);
        String refreshToken = response.substring(response.indexOf("refreshToken")+15, response.indexOf("accessTokenExpireTime")-3);

        TokenRequestDto tokenRequestDto = new TokenRequestDto(accessToken, refreshToken);

        MvcResult newAccessToken = mockMvc.perform(
                post("/auth/reissue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tokenRequestDto))
                        .header("Authorization", "Bearer " + accessToken)
        ).andExpect(status().isOk()).andReturn();

        System.out.println("기존 액세스, 리프레시 토큰 : " + response);
        System.out.println("재발급한 액세스, 리프레시 토큰 : " + newAccessToken.getResponse().getContentAsString());
        //오류 발생 => 액세스토큰 값이 변하지 않음
    }

}
