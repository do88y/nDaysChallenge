package challenge.nDaysChallenge.dajim;

import challenge.nDaysChallenge.controller.dajim.DajimController;
import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.dajim.Dajim;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class DajimTest {

    @Autowired
    DajimRepository dajimRepository;

    @DisplayName("다짐 저장하기")
    @Test
    void saveDajim(){
        //given
        Member member = new Member("user123","12345","userNickname",1,4, Authority.ROLE_USER);
        Room room = new Room("newRoom",new Period(100L), Category.ROUTINE, RoomType.GROUP,4);
        //RoomMember.createRoomMember(member, room);

        //when
        DajimRequestDto dajimRequestDto = new DajimRequestDto("다짐 내용");
        Dajim newDajim = Dajim.builder()
                .room(room)
                .member(member)
                .content(dajimRequestDto.getContent())
                .build();
        Dajim savedDajim = dajimRepository.save(newDajim);

        //then
        assertThat(newDajim.getMember().getId()).isEqualTo(savedDajim.getMember().getId());
    }


    //다짐 수정하기 (업데이트시간 필드값 변경 여부 확인)
    void 다짐수정(){

    }

}
