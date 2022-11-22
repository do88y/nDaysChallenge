package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Stickers;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.response.DajimResponseDto;
import challenge.nDaysChallenge.repository.dajim.DajimFeedRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DajimFeedService {

    private final DajimFeedRepository dajimFeedRepository;
    private final DajimRepository dajimRepository;

    //피드 전체 조회
    public List<Dajim> viewDajimOnFeed(Long roomNumber, UserDetailsImpl userDetailsImpl) {
        Member loggedInMember = userDetailsImpl.getMember(); //로그인 사용자

        List<Dajim> dajims = dajimFeedRepository.findAllByMemberAndOpen(loggedInMember); //룸 객체 => 같은 룸 및 공개된 다짐 전체 조회

        if (dajims==null){
            throw new RuntimeException("다짐을 확인할 수 없습니다."); //임시 RuntimeException
        }

        return dajims;
    }

}
