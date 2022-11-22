package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.dto.response.DajimResponseDto;
import challenge.nDaysChallenge.repository.dajim.DajimFeedRepository;
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

    //피드 전체 조회
    public List<DajimResponseDto> viewDajimOnFeed(Long roomNumber, UserDetailsImpl userDetailsImpl) {
        Member loggedInMember = userDetailsImpl.getMember(); //로그인 사용자

        List<Dajim> dajims = dajimFeedRepository.findAllByRoomNumberAndOpen(roomNumber); //다짐 조회
        //List<String> likes = dajimFeedRepository.findAllByDajimAndMember(dajim,userDetailsImpl.getMember()); //좋아요 조회

        if (dajims==null){
            throw new RuntimeException("다짐을 확인할 수 없습니다."); //임시 RuntimeException
        }

        List<DajimResponseDto> dajimsList = dajims.stream().map(dajim ->
                        new DajimResponseDto(userDetailsImpl.getMember().getNickname(),dajim.getContent()))
                .collect(Collectors.toList());

        return dajimsList;
    }

}
