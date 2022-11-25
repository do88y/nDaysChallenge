package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Stickers;
import challenge.nDaysChallenge.domain.room.GroupRoom;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.SingleRoom;
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
import java.util.stream.Stream;

@Service
@Transactional
@RequiredArgsConstructor
public class DajimFeedService {

    private final DajimFeedRepository dajimFeedRepository;

    //피드 전체 조회
    public List<Dajim> viewDajimOnFeed(UserDetailsImpl userDetailsImpl) {
        Member loggedInMember = userDetailsImpl.getMember(); //로그인 사용자

        //로그인 사용자가 소속된 챌린지 단체룸
        List<RoomMember> roomMemberList = loggedInMember.getRoomMemberList();
        List<Long> loggedInGroupRoomNumber = roomMemberList.stream().map(roomMember ->
                roomMember.getRoom().getNumber())
                .collect(Collectors.toList());

        //로그인 사용자가 소속된 챌린지 개인룸
        List<SingleRoom> singleRooms = loggedInMember.getSingleRooms();
        List<Long> loggedInSingleRoomNumber = singleRooms.stream().map(singleRoom ->
                        singleRoom.getNumber())
                .collect(Collectors.toList());

        //파라미터로 사용자가 소속된 룸 넘버 전달, 해당 룸넘버와 연결되는 다짐들 리턴
        List<Dajim> dajims = dajimFeedRepository.findAllByMemberAndOpen(loggedInGroupRoomNumber, loggedInSingleRoomNumber);

        if (dajims==null){
            throw new RuntimeException("다짐을 확인할 수 없습니다."); //임시 RuntimeException
        }

        return dajims;
    }

}
