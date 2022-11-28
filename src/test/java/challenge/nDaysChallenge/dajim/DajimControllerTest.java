package challenge.nDaysChallenge.dajim;

import challenge.nDaysChallenge.domain.Authority;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.List;

@AutoConfigureMockMvc
@SpringBootTest
public class DajimControllerTest {

//    @Autowired
//    MockMvc mockMvc;
//
//    @Test
//    @DisplayName("다짐 등록 테스트")
//    public void 다짐등록() throws Exception{
//        DajimRequestDto dajimRequestDto = new DajimRequestDto("내용","PUBLIC");
//        mockMvc.perform(
//                get("/challenge/1")
//        );
//    }

}
