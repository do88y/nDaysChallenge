package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import challenge.nDaysChallenge.dto.response.DajimFeedResponseDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.dajim.DajimFeedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DajimFeedService {

    private final DajimFeedRepository dajimFeedRepository;

    //피드 전체 조회
    public List<DajimFeedResponseDto> viewDajimOnFeed() {

        //open=PUBLIC인 다짐들 피드에 공개
        List<Dajim> dajims = dajimFeedRepository.findAllByOpen();

        //도메인->dto
        List<DajimFeedResponseDto> dajimFeedList = dajims.stream().map(dajim ->
                new DajimFeedResponseDto(
                        dajim.getNumber(),
                        dajim.getMember().getNickname(),
                        dajim.getContent(),
                        dajim.getEmotions().stream().map(emotion ->
                                        emotion.getStickers().toString())
                                .collect(Collectors.toList()),
                        dajim.getUpdatedDate()
                )).collect(Collectors.toList());

        return dajimFeedList;
    }

}
