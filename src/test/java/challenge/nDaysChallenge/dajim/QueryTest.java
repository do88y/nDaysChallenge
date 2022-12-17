package challenge.nDaysChallenge.dajim;

import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.dajim.DajimFeedRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
@Rollback(value = false)
public class QueryTest { //N+1 테스트

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

    @Test
    void 다짐10개_등록(){

    }

    @Test
    void 멤버10명_이모션2개씩_등록(){

    }

    @Test
    void 이모션10개_수정(){

    }

    @Test
    void 다짐피드_조회(){

    }

    @Test
    void 같은_챌린지룸_다짐_조회(){

    }

    @Test
    void 룸넘버로_룸_검색(){

    }

    @Test
    void 다짐넘버로_다짐_검색(){

    }

    //기타 멤버레포지토리

}
