package challenge.nDaysChallenge.security;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.Period;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import challenge.nDaysChallenge.dto.request.dajim.DajimUploadRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.dto.response.dajim.DajimFeedResponseDto;
import challenge.nDaysChallenge.dto.response.dajim.DajimResponseDto;
import challenge.nDaysChallenge.dto.response.member.MemberInfoResponseDto;
import challenge.nDaysChallenge.jwt.TokenProvider;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.jwt.RefreshTokenRepository;
import challenge.nDaysChallenge.repository.dajim.DajimFeedRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.repository.room.SingleRoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.transaction.BeforeTransaction;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback(value = true)
public class WithUserDetailsTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    SingleRoomRepository singleRoomRepository;

    @Autowired
    DajimRepository dajimRepository;

    @Autowired
    DajimFeedRepository dajimFeedRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @BeforeTransaction
    public void 회원가입() {
        MemberRequestDto memberRequestDto = new MemberRequestDto("abc@naver.com", "12345", "aaa", 1, 2);
        Member member = memberRequestDto.toMember(passwordEncoder);
        memberRepository.save(member);
    }

/*
    @Test
    @WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "abc@naver.com")
    public void 로그인() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto();
        loginRequestDto.setId("abc@naver.com");
        loginRequestDto.setPw("12345");
//        loginRequestDto.setPw(passwordEncoder.encode("12345"));

        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();

        //사용자 비밀번호 검증
        //CustomUserDetailsService의 loadUserByUsername 메소드 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String currentMemberId = SecurityContextHolder.getContext().getAuthentication().getName(); //시큐리티 컨텍스트에 저장된 id

        assertThat(currentMemberId).isEqualTo("abc@naver.com");
    }
*/

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "abc@naver.com")
    public void 다짐_작성_후_룸_조회() {
        //멤버 객체 가져오기
        Optional<Member> member = memberRepository.findById(SecurityContextHolder.getContext().getAuthentication().getName());
        Member currentMember = member.get();

        //룸 객체 연결
        SingleRoom singleRoom = new SingleRoom("roomName", new Period(LocalDate.now(),10L), Category.ROUTINE, 2, "reward", 0, 0);
        singleRoomRepository.save(singleRoom);
        Stamp stamp = Stamp.createStamp(singleRoom);
        singleRoom.addRoom(singleRoom, currentMember, stamp);

        //다짐 작성
        DajimUploadRequestDto dajimUploadRequestDto = new DajimUploadRequestDto("다짐 내용", "PUBLIC");
        Dajim newDajim = Dajim.builder()
                .room(singleRoom)
                .member(currentMember)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build();
        Dajim savedDajim = dajimRepository.save(newDajim);

        //룸에서 다짐 조회
        List<Dajim> dajims = dajimRepository.findAllByRoomNumber(1L).orElseThrow(()-> new RuntimeException("다짐을 확인할 수 없습니다."));

        List<DajimResponseDto> dajimsList = dajims.stream().map(dajim ->
                        new DajimResponseDto(
                                savedDajim.getNumber(),
                                savedDajim.getMember().getNickname(),
                                savedDajim.getMember().getImage(),
                                savedDajim.getContent(),
                                savedDajim.getOpen().toString(),
                                savedDajim.getUpdatedDate()))
                .collect(Collectors.toList());

        assertThat(dajimsList.size()).isEqualTo(1);
        assertThat(dajimsList.get(0).getContent()).isEqualTo(savedDajim.getContent());

        //피드에서 다짐 조회
        List<Dajim> dajimFeed = dajimFeedRepository.findAllByOpen();

        List<DajimFeedResponseDto> dajimFeedList = dajimFeed.stream().map(d ->
                new DajimFeedResponseDto(
                        d.getNumber(),
                        d.getMember().getNickname(),
                        d.getMember().getImage(),
                        d.getContent(),
                        d.getEmotions().stream().map(emotion ->
                                        emotion.getSticker().toString())
                                .collect(Collectors.toList()),
                        d.getUpdatedDate()
                )).collect(Collectors.toList());

        assertThat(dajimFeedList.get(0).getContent()).isEqualTo("다짐 내용");
    }

    @Test
    @WithUserDetails(userDetailsServiceBeanName = "customUserDetailsService", value = "abc@naver.com")
    public void 회원_정보_조회() {
        String currentMemberId = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> member = memberRepository.findById(currentMemberId);
        Member member1 = member.get();

        MemberInfoResponseDto dto = MemberInfoResponseDto.of(member1);

        assertThat(dto.getId()).isEqualTo("abc@naver.com");
    }
}