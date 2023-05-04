package challenge.nDaysChallenge.member;

import challenge.nDaysChallenge.NDaysChallengeApplication;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.dto.request.member.MemberEditRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.service.jwt.AuthService;
import challenge.nDaysChallenge.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = NDaysChallengeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class MemberIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @BeforeTransaction
    public void 회원_세팅(){
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc123@naver.com", "123", "닉네임", 4);

        authService.signup(memberRequestDto);
    }

    @AfterTransaction
    public void 회원_삭제(){
        memberService.deleteMember("abc123@naver.com");
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "abc123@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void 회원정보_조회() throws Exception{
        mockMvc.perform(
                get("/user/details")
        ).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "abc123@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void 회원정보_수정() throws Exception{
        MemberEditRequestDto memberEditRequestDto = new MemberEditRequestDto("1234", "내루미", 3);

        mockMvc.perform(
                patch("/user/edit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(memberEditRequestDto))
        ).andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "abc123@naver.com", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    public void 회원_탈퇴() throws Exception{
        mockMvc.perform(
                delete("/user/withdrawal")
        ).andExpect(status().isOk());
    }

}
