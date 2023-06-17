package challenge.nDaysChallenge.dajim;

import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.dajim.DajimUploadRequestDto;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DajimRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    DajimRepository dajimRepository;

    Member savedMember1, savedMember2;
    Room savedRoom;
    Dajim savedDajim1, savedDajim2;

    @BeforeEach
    void 멤버_룸_다짐_세팅(){
        Member member1 = Member.builder()
                .authority(Authority.ROLE_USER)
                .id("user@naver.com")
                .pw("1234")
                .nickname("nick")
                .image(3)
                .build();

        Member member2 = Member.builder()
                .authority(Authority.ROLE_USER)
                .id("user2@naver.com")
                .pw("1234")
                .nickname("nick2")
                .image(3)
                .build();

        savedMember1 = memberRepository.save(member1);
        savedMember2 = memberRepository.save(member2);

        Room room = new SingleRoom("기상", new Period(LocalDate.now(),30) , Category.ROUTINE,2, "");

        savedRoom = roomRepository.save(room);

        Dajim dajim1 = DajimUploadRequestDto.toDajim(room, member1, "내용1", "PUBLIC");
        Dajim dajim2 = DajimUploadRequestDto.toDajim(room, member2, "내용2", "PUBLIC");

        savedDajim1 = dajimRepository.save(dajim1);
        savedDajim2 = dajimRepository.save(dajim2);
    }

    @Test
    void 챌린지룸_다짐_조회(){
        List<Dajim> dajims = dajimRepository.findAllByRoomNumber(savedDajim1.getRoom().getNumber())
                .orElseThrow(()->new RuntimeException("해당 룸에서 다짐을 조회할 수 없습니다."));

        System.out.println(dajims.stream().map(dajim -> dajim.getContent())
                                            .collect(Collectors.toList()));

        assertThat(dajims.size()).isEqualTo(2);}

    @Test
    void 다짐번호로_다짐_조회(){
        Dajim dajim1 = dajimRepository.findByMember_IdAndRoom_Number(savedDajim1.getMember().getId(),savedDajim1.getRoom().getNumber())
                .orElseThrow(()->new RuntimeException("다짐이 없습니다"));
        Dajim dajim2 = dajimRepository.findByMember_IdAndRoom_Number(savedDajim2.getMember().getId(),savedDajim2.getRoom().getNumber())
                .orElseThrow(()->new RuntimeException("다짐이 없습니다"));

        assertThat(dajim1.getMember().getNumber()).isEqualTo(savedDajim1.getMember().getNumber());
        assertThat(dajim2.getMember().getNumber()).isEqualTo(savedDajim2.getMember().getNumber());
    }

    @Test
    void 특정_멤버의_다짐_전체_조회(){
        List<Dajim> nickDajims = dajimRepository.findAllByMember_Nickname("nick2")
                .orElseThrow(()->new RuntimeException("해당 멤버에게 다짐이 없습니다."));

        assertThat(nickDajims.size()).isEqualTo(1);

        System.out.println(nickDajims.stream().map(dajim ->
                        dajim.getContent())
                .collect(Collectors.toList()));
    }

    @Test
    void 다짐_피드_조회(){
        Slice<Dajim> dajimPage = dajimRepository.findByOpen(Open.PUBLIC,Pageable.ofSize(10));

        assertThat(dajimPage.getContent().size()).isEqualTo(2);
    }

}
