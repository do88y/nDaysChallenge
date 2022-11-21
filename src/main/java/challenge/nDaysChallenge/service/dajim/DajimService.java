package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import challenge.nDaysChallenge.dto.response.DajimResponseDto;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class DajimService {

    private final DajimRepository dajimRepository;

    //다짐 업로드
    public Long uploadDajim(DajimRequestDto requestDto, UserDetailsImpl userDetailsImpl) {
        Member member = userDetailsImpl.getMember();
        String content = requestDto.getContent();

        Dajim dajim = new Dajim();
        dajim.builder()
                .number(dajim.getNumber())
                .member(member)
                .content(content)
                .build();

        Dajim savedDajim = dajimRepository.save(dajim);
        Long savedDajimNumber = savedDajim.getNumber();

        return savedDajimNumber;
    }

    //챌린지 상세 조회 (같은 룸)
    public Map<Long, DajimResponseDto> viewDajimOnChallenge(Long roomNumber, UserDetailsImpl userDetailsImpl) {
        Member loggedInMember = userDetailsImpl.getMember(); //로그인 사용자

        Optional<Dajim> dajims = null;

        try {
            dajims = dajimRepository.findAllByRoomNumber(roomNumber);
        } catch (Exception e) {
            throw new RuntimeException("다짐을 확인할 수 없습니다."); //임시 RuntimeException
        }

//        Map<Long, DajimResponseDto> dajimResponseDtoMap = dajims.map(dajim ->
//                new DajimResponseDto(loggedInMember.getNickname(), dajim.getContent()));

        return dajimResponseDtoMap;
    }

    //피드 전체 조회
    public List<Dajim> viewDajimOnFeed(UserDetailsImpl userDetailsImpl) {
        Member member = userDetailsImpl.getMember();


    }

}
