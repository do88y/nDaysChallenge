package challenge.nDaysChallenge.dajim;

import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.dajim.DajimUploadRequestDto;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class DajimRepositoryTest {

    @Autowired DajimRepository dajimRepository;

    @DisplayName("다짐 저장")
    @Test
    void saveDajim(){
        //given
        Member member = new Member("user@naver.com","12345","userN",1, Authority.ROLE_USER);

        GroupRoom room = new GroupRoom(member, "newRoom",new Period(LocalDate.now(),100L), Category.ROUTINE,4,"보상", 0, 0);

        //when
        DajimUploadRequestDto dajimUploadRequestDto = new DajimUploadRequestDto("다짐 내용", "PUBLIC");
        Dajim newDajim = Dajim.builder()
                .room(room)
                .member(member)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build();
        Dajim savedDajim = dajimRepository.save(newDajim);

        checkDajimRoomUser(newDajim, room, member);

        //then
        assertThat(newDajim.getMember().getId()).isEqualTo(savedDajim.getMember().getId());
    }

    private static void checkDajimRoomUser(Dajim dajim, Room room, Member member){
        if (dajim.getRoom()!=room || dajim.getMember()!=member){
            throw new RuntimeException("다짐에 대한 권한이 없습니다.");
        }
    }

    @DisplayName("다짐 수정")
    @Test
    void modifyDajim(){
        //given
        Member member = new Member("user@naver.com","12345","userN",1, Authority.ROLE_USER);

        GroupRoom room = new GroupRoom(member, "newRoom",new Period(LocalDate.now(),100L), Category.ROUTINE,4,"보상", 0, 0);
        DajimUploadRequestDto dajimUploadRequestDto = new DajimUploadRequestDto("다짐 내용", "PUBLIC");
        Dajim dajim = Dajim.builder()
                .room(room)
                .member(member)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build();
        dajimRepository.save(dajim);

        //when
        Dajim newdajim = dajimRepository.findByDajimNumber(1L).orElseThrow(()->new RuntimeException("현재 다짐을 찾을 수 없습니다."));
        Dajim updatedDajim = newdajim.update(Open.PRIVATE, "새 다짐");

        //then
        assertThat(updatedDajim.getContent()).isEqualTo("새 다짐");
    }

    @DisplayName("전체 룸멤버 다짐 조회")
    @Test
    void viewDajims(){
        //given
        Member member = new Member("user@naver.com","12345","userN",1, Authority.ROLE_USER);
        Member member2 = new Member("user2@naver.com","12345","userN2",1, Authority.ROLE_USER);
        GroupRoom room = new GroupRoom(member, "newRoom",new Period(LocalDate.now(),100L), Category.ROUTINE,4, "", 0, 0);

        DajimUploadRequestDto dajimUploadRequestDto = new DajimUploadRequestDto("다짐 내용", "PUBLIC");

        Dajim newDajim = Dajim.builder()
                .room(room)
                .member(member)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build();
        dajimRepository.save(newDajim);

        Dajim newDajim2 = Dajim.builder()
                .room(room)
                .member(member2)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build();
        dajimRepository.save(newDajim2);

        //when
        List<Dajim> dajimList = dajimRepository.findAllByRoomNumber(room.getNumber()).orElseThrow(()-> new RuntimeException("다짐을 확인할 수 없습니다."));

        //then
        assertThat(dajimList.size()).isEqualTo(2);
    }

}
