package challenge.nDaysChallenge.auth;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.transaction.BeforeTransaction;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class AuthServiceTest {
//
//    @BeforeTransaction
//    public void 회원가입(){
//        MemberRequestDto memberRequestDto = new MemberRequestDto("abc@naver.com","123","aaa",1);
//        Member member = memberRequestDto.toMember(passwordEncoder);
//        memberRepository.save(member);
//    }
//
//    @Test
//    void 닉네임_중복확인(){
//        //닉네임 중복 확인
//        boolean exists = memberRepository.existsByNickname("aaa");
//        boolean exists2 = memberRepository.existsByNickname("new");
//
//        assertThat(exists).isEqualTo(true);
//        assertThat(exists2).isEqualTo(false);
//    }
//
//    @Test
//    void 아이디_중복_검사(){
//        //mocking
//        given(memberRepository.existsById("abc@naver.com")).willReturn(true);
//        given(memberRepository.existsById("abcd@naver.com")).willReturn(false);
//
//        //when
//        String id = authService.idCheck("abc@naver.com");
//        String id2 = authService.idCheck("abcd@naver.com");
//
//        //then
//        assertThat(id).isEqualTo("exists");
//        assertThat(id2).isEqualTo("ok");
//    }
//
//    @Test
//    void 닉네임_중복_검사(){
//        //mocking
//        boolean exists = memberRepository.existsByNickname("aaa");
//        memberRepository.existsByNickname("aaab");
//
//        //when
//        String nickname = authService.nicknameCheck("aaa");
//        String nickname2 = authService.nicknameCheck("aaab");
//
//        //then
//        assertThat(nickname).isEqualTo("exists");
//        assertThat(nickname2).isEqualTo("ok");
//    }
//
//    @Test
//    void 회원_탈퇴(){
//        //when
//        Member memberFound = memberRepository.findByNickname("aaa")
//                .orElseThrow(()->new RuntimeException("해당 닉네임의 회원이 없습니다."));
//
//        memberRepository.delete(memberFound);
//
//        //then
//        assertThat(memberRepository.existsByNickname("aaa")).isFalse();
//    }

}
