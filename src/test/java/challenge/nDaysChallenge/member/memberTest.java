package challenge.nDaysChallenge.member;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.room.RoomMember;
import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.dajim.Sticker;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.dajim.DajimUploadRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import challenge.nDaysChallenge.repository.room.GroupRoomRepository;
import challenge.nDaysChallenge.repository.room.SingleRoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class memberTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GroupRoomRepository groupRoomRepository;

    @Autowired
    SingleRoomRepository singleRoomRepository;

    @Autowired
    private RoomMemberRepository roomMemberRepository;

    @Autowired
    private DajimRepository dajimRepository;

    @Autowired
    EmotionRepository emotionRepository;

    @BeforeTransaction
    public void 회원가입(){
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc@naver.com","123","aaa",1);
        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);
    }

    @DisplayName("닉네임 중복 확인")
    @Test
    void 닉네임_중복확인(){
        //닉네임 중복 확인
        boolean exists = memberRepository.existsByNickname("aaa");
        boolean exists2 = memberRepository.existsByNickname("new");

        assertThat(exists).isEqualTo(true);
        assertThat(exists2).isEqualTo(false);
    }

    @DisplayName("아이디 중복 확인")
    @Test
    void 아이디_중복확인(){
        //닉네임 중복 확인
        boolean exists = memberRepository.existsById("abc@naver.com");
        boolean exists2 = memberRepository.existsById("aaa@naver.com");

        assertThat(exists).isEqualTo(true);
        assertThat(exists2).isEqualTo(false);
    }

    @DisplayName("멤버 닉네임 변경")
    @Test
    void 닉네임_변경(){
        //회원가입
        Member member = memberRepository.findById("abc@naver.com")
                .orElseThrow(()->new RuntimeException("멤버를 찾을 수 없습니다."));

        //닉네임 변경
        Member updatedMember = member.update("새 닉네임","123",2);

        assertThat(updatedMember.getNickname()).isEqualTo("새 닉네임");
        assertThat(updatedMember.getPw()).isEqualTo("123");
        assertThat(updatedMember.getImage()).isEqualTo(2);
    }

    @Test
    void 회원_탈퇴(){
        //when
        Member memberFound = memberRepository.findByNickname("aaa")
                .orElseThrow(()->new RuntimeException("해당 닉네임의 회원이 없습니다."));

        memberRepository.delete(memberFound);

        //then
        assertThat(memberRepository.existsByNickname("aaa")).isFalse();
    }

}

