package challenge.nDaysChallenge.dajim;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.Period;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.RoomType;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
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
        Room room = new Room("newRoom",new Period(100L), Category.ROUTINE, RoomType.GROUP,4);
        Member member = new Member("user@naver.com","12345","userN",1,4, Authority.ROLE_USER);

        //when
        DajimRequestDto dajimRequestDto = new DajimRequestDto("다짐 내용", "PUBLIC");
        Dajim newDajim = Dajim.builder()
                .room(room)
                .member(member)
                .content(dajimRequestDto.getContent())
                .open(Open.valueOf(dajimRequestDto.getOpen()))
                .build();
        Dajim savedDajim = dajimRepository.save(newDajim);

        //then
        assertThat(newDajim.getMember().getId()).isEqualTo(savedDajim.getMember().getId());
    }


    @DisplayName("다짐 수정")
    @Test
    void modifyDajim(){
        //given
        Room room = new Room("newRoom",new Period(100L), Category.ROUTINE, RoomType.GROUP,4);
        Member member = new Member("user@naver.com","12345","userN",1,4, Authority.ROLE_USER);
        DajimRequestDto dajimRequestDto = new DajimRequestDto("다짐 내용", "PUBLIC");
        Dajim dajim = Dajim.builder()
                .room(room)
                .member(member)
                .content(dajimRequestDto.getContent())
                .open(Open.valueOf(dajimRequestDto.getOpen()))
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
        Member member = new Member("user@naver.com","12345","userN",1,4, Authority.ROLE_USER);
        Member member2 = new Member("user2@naver.com","12345","userN2",1,4, Authority.ROLE_USER);
        Room room = new Room("newRoom",new Period(100L), Category.ROUTINE, RoomType.GROUP,4);

        DajimRequestDto dajimRequestDto = new DajimRequestDto("다짐 내용", "PUBLIC");

        Dajim newDajim = Dajim.builder()
                .room(room)
                .member(member)
                .content(dajimRequestDto.getContent())
                .open(Open.valueOf(dajimRequestDto.getOpen()))
                .build();
        dajimRepository.save(newDajim);

        Dajim newDajim2 = Dajim.builder()
                .room(room)
                .member(member2)
                .content(dajimRequestDto.getContent())
                .open(Open.valueOf(dajimRequestDto.getOpen()))
                .build();
        dajimRepository.save(newDajim2);

        //when
        List<Dajim> dajimList = dajimRepository.findAllByRoomNumber(room.getNumber());

        //then
        assertThat(dajimList.size()).isEqualTo(2);
    }

}
