package challenge.nDaysChallenge.member;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.dto.request.jwt.LoginRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    private Member member;

    @BeforeEach
    public void 회원가입_로그인_세팅() throws Exception {
        //회원가입
        MemberRequestDto memberRequestDto = new MemberRequestDto("testid@naver.com", "123", "testnickname", 4);
        member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);

        //로그인
        LoginRequestDto loginRequestDto = new LoginRequestDto("testid@naver.com","123");

        MvcResult result = mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        ).andExpect(status().isOk()).andReturn();

        String response = result.getResponse().getContentAsString(); //로그인 시 받은 Responsebody
        accessToken = response.substring(response.indexOf("accessToken")+14, response.indexOf("refreshToken")-3); //중 AccessToken 값만 요청 헤더에 넣기
    }

    @AfterEach
    public void 세팅_회원_삭제(){
        memberRepository.delete(member);
    }

    @Test
    @WithMockUser(value = "tesid@naver.com")
    public void 회원정보_조회() throws Exception {
        mockMvc.perform(
                get("/user/details")
                        .header("Authorization","Bearer " + accessToken)
        ).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(value = "tesid@naver.com")
    public void 회원정보_수정() throws Exception {
        Map<String, String> request = new HashMap<>();
        request.put("pw","12345");
        request.put("nickname","newnickname");
        request.put("image","2");

        mockMvc.perform(
                patch("/user/edit")
                        .header("Authorization","Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(value = "tesid@naver.com")
    public void 회원_삭제() throws Exception {
        mockMvc.perform(
                delete("/user/withdrawal")
                        .header("Authorization","Bearer " + accessToken)
        ).andExpect(status().isOk()).andDo(print());
    }

}
