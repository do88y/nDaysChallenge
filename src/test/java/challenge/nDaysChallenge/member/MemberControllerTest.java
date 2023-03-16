package challenge.nDaysChallenge.member;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.dto.request.jwt.LoginRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    private String accessToken;

    @BeforeEach
    public void 회원가입_로그인_세팅() throws Exception {
        //회원가입
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc12@naver.com", "123", "닉네임3", 4);
        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);

        //로그인
        LoginRequestDto loginRequestDto = new LoginRequestDto("abc12@naver.com","123");

        MvcResult result = mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        ).andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString(); //로그인 시 받은 Responsebody
        accessToken = response.substring(response.indexOf("accessToken")+14, response.indexOf("refreshToken")-3); //중 AccessToken 값만 요청 헤더에 넣기
    }

    @Test
    @WithMockUser(value = "abc12@naver.com")
    public void 회원정보_조회() throws Exception {
        mockMvc.perform(
                get("/user/details")
                        .header("Authorization","Bearer "+accessToken)
        ).andExpect(status().isOk());
    }

}
