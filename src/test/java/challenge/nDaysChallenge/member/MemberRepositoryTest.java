package challenge.nDaysChallenge.member;

import challenge.nDaysChallenge.config.SecurityConfig;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.transaction.BeforeTransaction;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void 회원가입(){
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc321@naver.com","123","aaa1",1);
        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);
    }

    @Test
    void 닉네임_중복_검사(){
        //닉네임 중복 확인
        boolean nicknameTrue = memberRepository.existsByNickname("aaa1");
        boolean nicknameFalse = memberRepository.existsByNickname("new");

        assertThat(nicknameTrue).isTrue();
        assertThat(nicknameFalse).isFalse();
    }

    @Test
    void 아이디_중복_검사(){
        boolean idTrue = memberRepository.existsById("abc321@naver.com");
        boolean idFalse = memberRepository.existsById("abcde@naver.com");

        assertThat(idTrue).isTrue();
        assertThat(idFalse).isFalse();
    }

    @Test
    void 회원_탈퇴(){
        //given
        Member memberFound = memberRepository.findByNickname("aaa1")
                .orElseThrow(()->new RuntimeException("해당 닉네임의 회원이 없습니다."));

        //when
        memberRepository.delete(memberFound);

        //then
        assertThat(memberRepository.existsByNickname("aaa1")).isFalse();
    }

}
