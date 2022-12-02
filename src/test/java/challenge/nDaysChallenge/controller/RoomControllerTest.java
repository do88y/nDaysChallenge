package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.RoomRequestDTO;
import challenge.nDaysChallenge.dto.response.RoomResponseDto;
import challenge.nDaysChallenge.service.RoomService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
@SpringBootTest
class RoomControllerTest {

    //==MockMvc의 역할==//
    //perform: 가상의 request를 처리-> 요청은 MockHttpServletRequestBuilder 통해 생성
    //expect: 가상의 response에 대해 검증-> 검증 항목은 ResultMatchers를 반환하는 Handler(), status(), view() 등 메서드
    //do: 테스트 과정에서 콘솔 출력 등 직접 처리할 일을 작성-> 실제 동작은 ResultHandler 사용

    Member member = new Member("user@naver.com", "12345", "nick", 1, 4, Authority.ROLE_USER);
    RoomRequestDTO dto = new RoomRequestDTO("안녕", Category.ROUTINE.name(), 5, 30L, RoomType.SINGLE.name(), 1L);
    SingleRoom room = new SingleRoom(dto.getName(), new Period(dto.getTotalDays()), Category.valueOf(dto.getCategory()), dto.getPassCount(), "잠");


    @Mock
    private User user;
    @Autowired
    private MockMvc mock;

    @Autowired EntityManager em;
    @Autowired RoomService roomService;

/*
    @Test
    @Transactional
    public void createRoom() throws Exception {
        //given
        em.persist(member);
        user = (User) User.builder()
                        .username(member.getId())
                        .password(member.getPw())
                        .authorities("USER")
                        .build();

        //when
        Room newRoom = roomService.createRoom(member, dto);
        RoomResponseDto savedRoom = RoomResponseDto.builder()
                .name(newRoom.getName())
                .category(newRoom.getCategory().name())
                .reward(newRoom.getReward())
                .type(newRoom.getType().name())
                .status(newRoom.getStatus().name())
                .passCount(newRoom.getPassCount())
                .totalDays(newRoom.getPeriod().getTotalDays())
                .build();

        //then
        mock.perform(post("/api/challenge/create"))
                .andExpect(status().isCreated());
    }
*/

    @Test
    @Transactional
    public void createRoom_싱글챌린지() throws Exception {
        //given
        em.persist(member);
        user = (User) User.builder()
                        .username(member.getId())
                        .password(member.getPw())
                        .authorities("USER")
                        .build();

        //when
        Room newRoom = roomService.createRoom(member, dto);

        //then
        assertThat(newRoom.getName()).isEqualTo(room.getName());
    }



}