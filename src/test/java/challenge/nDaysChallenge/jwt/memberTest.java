package challenge.nDaysChallenge.jwt;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.dajim.Stickers;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import challenge.nDaysChallenge.dto.request.MemberRequestDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class memberTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomMemberRepository roomMemberRepository;

    @Autowired
    private DajimRepository dajimRepository;

    @Autowired
    EmotionRepository emotionRepository;

    @BeforeTransaction
    @Transactional
    @Rollback(value = false)
    public void 회원가입(){
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc@naver.com","123","aaa",1,2);
        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);
    }

    @DisplayName("닉네임 중복 확인")
    @Test
    @Transactional
    @Rollback(value = false)
    void 닉네임_중복확인(){
        //닉네임 중복 확인
        boolean exists = memberRepository.existsByNickname("aaa");
        boolean exists2 = memberRepository.existsByNickname("new");

        assertThat(exists).isEqualTo(true);
        assertThat(exists2).isEqualTo(false);
    }

    @DisplayName("멤버 닉네임 변경")
    @Test
    @Transactional
    @Rollback(value = false)
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


    @DisplayName("멤버 삭제(탈퇴) 시 룸/다짐/이모션 존재 여부 확인")
    @Test
    @Transactional
    @Rollback(value = false)
    void 회원_탈퇴(){
        //멤버
        Member member1 = Member.builder()
                .id("user@naver.com")
                .pw("12345")
                .nickname("userN")
                .image(1)
                .roomLimit(4)
                .authority(Authority.ROLE_USER)
                .build();

        DajimRequestDto dajimRequestDto = new DajimRequestDto(null,"다짐 내용", "PRIVATE");

        //싱글룸
        SingleRoom room1 = new SingleRoom("SingleRoom", new Period(LocalDate.now(),10L), Category.ROUTINE, 2, "", 0, 0);
        roomRepository.save(room1);
        SingleRoom singleRoom1 = room1.addRoom(room1, member1);
        roomRepository.save(singleRoom1);

        //그룹룸
        GroupRoom room2 = new GroupRoom("GroupRoom", new Period(LocalDate.now(),100L), Category.ETC, 3, "", 0, 0);
        roomRepository.save(room2);
        RoomMember roomMember1 = RoomMember.createRoomMember(member1, room2);
        roomMemberRepository.save(roomMember1);

        //다짐 1, 2
        Dajim dajim1 = dajimRepository.save(Dajim.builder()
                .room(room1)
                .member(member1)
                .content(dajimRequestDto.getContent())
                .open(Open.valueOf(dajimRequestDto.getOpen()))
                .build());

        Dajim dajim2 = dajimRepository.save(Dajim.builder()
                .room(room2)
                .member(member1)
                .content(dajimRequestDto.getContent())
                .open(Open.valueOf(dajimRequestDto.getOpen()))
                .build());

        //감정 1, 2
        Emotion emotion = Emotion.builder()
                .member(member1)
                .dajim(dajim1)
                .stickers(Stickers.valueOf("TOUCHED"))
                .build();

        Emotion emotion2 = Emotion.builder()
                .member(member1)
                .dajim(dajim2)
                .stickers(Stickers.valueOf("SURPRISE"))
                .build();

        Emotion savedEmotion = emotionRepository.save(emotion);
        Emotion savedEmotion2 = emotionRepository.save(emotion2);

        //다짐-감정 연결
        dajim1.addEmotions(savedEmotion);
        dajim2.addEmotions(savedEmotion2);

        //삭제
        memberRepository.delete(member1);

        System.out.println(dajim1.getMember().getId());
        System.out.println(emotion2.getMember().getId());
        System.out.println(roomMember1.getMember().getId());
    }

}

