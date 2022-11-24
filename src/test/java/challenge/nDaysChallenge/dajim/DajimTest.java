package challenge.nDaysChallenge.dajim;

import challenge.nDaysChallenge.controller.dajim.DajimController;
import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.Period;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.RoomType;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.security.UserDetailsImpl;
import challenge.nDaysChallenge.service.dajim.DajimService;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
@Rollback(value = false)
class DajimTest {

    @Autowired
    DajimRepository dajimRepository;

    Dajim newDajim;

    @DisplayName("다짐 저장하기")
    @Test
    void saveDajim(){
        //given
        Member member = new Member("user@naver.com","12345","userN",1,4, Authority.ROLE_USER);
        Room room = new Room("newRoom",new Period(100L), Category.ROUTINE, RoomType.GROUP,4);

        //when
        DajimRequestDto dajimRequestDto = new DajimRequestDto("다짐 내용", Open.PUBLIC);
        newDajim = Dajim.builder()
                .room(room)
                .member(member)
                .content(dajimRequestDto.getContent())
                .open(dajimRequestDto.getOpen())
                .build();
        Dajim savedDajim = dajimRepository.save(newDajim);

        //then
        assertThat(newDajim.getMember().getId()).isEqualTo(savedDajim.getMember().getId());
    }


    //다짐 수정하기 (업데이트시간 필드값 변경 여부 확인)
    @Test
    void modifyDajim(){
        Member member = new Member("user@naver.com","12345","userN",1,4, Authority.ROLE_USER);
        Room room = new Room("newRoom",new Period(100L), Category.ROUTINE, RoomType.GROUP,4);

        Dajim dajim = dajimRepository.findByDajimNumber(1L);
        Dajim savedDajim = dajim.update(Open.PRIVATE, "새 다짐");

        assertThat(savedDajim.getContent()).isEqualTo("새 다짐");
    }

}
