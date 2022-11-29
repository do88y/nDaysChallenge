package challenge.nDaysChallenge.jwt;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.dto.request.SignupDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.service.AuthService;
import lombok.Builder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
public class loginTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("멤버 회원가입")
    @Test
    @Transactional
    @Rollback(value = false)
    void 회원가입(){
        SignupDto signupDto = new SignupDto("abc@naver.com","123","aaa",1,2);
        Member member = signupDto.toMember(passwordEncoder);
        memberRepository.save(member);
    }
}
