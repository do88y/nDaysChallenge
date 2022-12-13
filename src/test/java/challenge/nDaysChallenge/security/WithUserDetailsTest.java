package challenge.nDaysChallenge.security;

import challenge.nDaysChallenge.config.SecurityConfig;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.dto.request.MemberRequestDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Rollback(value = true)
public class WithUserDetailsTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

//    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
//    @Autowired
//    MockMvc mockMvc;

    @BeforeTransaction
    public void 회원가입(){
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc@naver.com","123","aaa",1,2);
        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);
    }

/*
    @Test
    @org.springframework.security.test.context.support.WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "abc@naver.com")
    public void 시큐리티컨텍스트_유저_꺼내기(){
        String currentMemberId = SecurityUtil.getCurrentMemberId(); //시큐리티 컨텍스트에 저장된 id

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        System.out.println(user.getUsername());

        assertThat(currentMemberId).isEqualTo("abc@naver.com");
    }
*/

}
