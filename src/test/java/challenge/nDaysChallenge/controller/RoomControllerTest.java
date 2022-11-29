package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
@AutoConfigureMockMvc
@SpringBootTest
class RoomControllerTest {

    @Autowired RoomController roomController;
    //==MockMvc의 역할==//
    //perform: 가상의 request를 처리-> 요청은 MockHttpServletRequestBuilder 통해 생성
    //expect: 가상의 response에 대해 검증-> 검증 항목은 ResultMatchers를 반환하는 Handler(), status(), view() 등 메서드
    //do: 테스트 과정에서 콘솔 출력 등 직접 처리할 일을 작성-> 실제 동작은 ResultHandler 사용
    @Autowired private MockMvc mock;

    @BeforeEach
    //테스트 실행 때마다 초기화
    public void setup() {
        mock = MockMvcBuilders.standaloneSetup(roomController).build();  //standalongSetup에 여러 Controller 등록 가능
    }


    @DisplayName("챌린지 생성")
    @Test
    public void createRoom() throws Exception {
        //given
//        given(content)
        //when

        //then
    }
}