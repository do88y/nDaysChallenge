package challenge.nDaysChallenge.security;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.dto.request.MemberEditRequestDto;
import challenge.nDaysChallenge.dto.request.MemberRequestDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.SelectorResolutionResult;
import org.junit.platform.engine.TestExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@Rollback(value = false)
public class AuthenticationPrincipalTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeTransaction
    public void 회원가입(){
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc@naver.com","123","aaa",1,2);
        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);
    }

    @BeforeTransaction
    public void setup(){
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

/*
    @Test
    @org.springframework.security.test.context.support.WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "abc@naver.com")
    public void 회원정보_수정() throws Exception {

        Optional<Member> member = memberRepository.findById("abc@naver.com");
        Member member1 = member.get();

        MemberEditRequestDto memberEditRequestDto = new MemberEditRequestDto("2222", "aaa", 2);

        ResultActions result = mockMvc.perform(put("/user/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8")
                .principal(new UsernamePasswordAuthenticationToken(member1, null))
                .content((objectMapper.writeValueAsString(memberEditRequestDto)))
        );

        result.andExpect(status().isOk());

    }
*/

}
