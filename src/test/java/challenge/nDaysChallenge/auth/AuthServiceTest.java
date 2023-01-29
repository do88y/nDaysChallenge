package challenge.nDaysChallenge.auth;

import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.service.jwt.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void 회원가입(){

    }

    @Test
    public void 로그인(){

    }

    @Test
    public void 로그아웃(){

    }

    @Test
    void 아이디_중복_검사(){

    }

    @Test
    void 닉네임_중복_검사(){

    }

    @Test
    void 회원_탈퇴(){

    }

}
