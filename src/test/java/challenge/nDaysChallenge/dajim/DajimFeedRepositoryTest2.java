package challenge.nDaysChallenge.dajim;

import challenge.nDaysChallenge.config.SecurityConfig;
import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.dajim.Stickers;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import challenge.nDaysChallenge.dto.request.EmotionRequestDto;
import challenge.nDaysChallenge.dto.request.MemberRequestDto;
import challenge.nDaysChallenge.dto.response.EmotionResponseDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.dajim.DajimFeedRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DajimFeedRepositoryTest2{

    @Autowired
    DajimRepository dajimRepository;

    @Autowired
    DajimFeedRepository dajimFeedRepository;

    @Autowired
    EmotionRepository emotionRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomMemberRepository roomMemberRepository;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("특정 멤버 피드 조회")
    @Test
    @Transactional
    @Rollback(value = false)
    void 멤버_피드_조회(){
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc@naver.com","123","aaa",1,2);
        Member member = memberRequestDto.toMember(SecurityConfig.passwordEncoder()); ///
        memberRepository.save(member);

        DajimRequestDto dajimRequestDto = new DajimRequestDto(null,"다짐 내용", "PRIVATE");

        //싱글룸 (룸1-멤버1)
        Room room1 = SingleRoom.builder()
                .name("SingleRoom")
                .period(new Period(10L))
                .category(Category.ROUTINE)
                .type(RoomType.SINGLE)
                .passCount(2)
                .build();
        roomRepository.save(room1);

        //싱글룸 다짐
        Dajim dajim = dajimRepository.save(Dajim.builder()
                .room(room1)
                .member(member)
                .content(dajimRequestDto.getContent())
                .open(Open.valueOf(dajimRequestDto.getOpen()))
                .build());

        //그룹룸 (룸2-멤버1,2,3)
        Room room2 = GroupRoom.builder()
                .name("GroupRoom")
                .period(new Period(100L))
                .category(Category.ETC)
                .type(RoomType.GROUP)
                .passCount(3)
                .build();
        roomRepository.save(room2);
        RoomMember roomMember = RoomMember.createRoomMember(member, room2);
        roomMemberRepository.save(roomMember);

        Dajim dajim2 = dajimRepository.save(Dajim.builder()
                .room(room2)
                .member(member)
                .content(dajimRequestDto.getContent())
                .open(Open.valueOf(dajimRequestDto.getOpen()))
                .build());
    }

}
