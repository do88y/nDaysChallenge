package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.room.Room;
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
@Transactional
@RequiredArgsConstructor
public class DajimFeedService {

    private final DajimFeedRepository dajimFeedRepository;

    private final MemberRepository memberRepository;

    //피드 전체 조회
    public List<DajimFeedResponseDto> viewDajimOnFeed(Member member) {
        Member loggedInMember = memberRepository.findById(member.getId())
                .orElseThrow(()->new RuntimeException("로그인한 사용자 정보를 찾을 수 없습니다."));
        //로그인 사용자가 소속된 챌린지 단체룸
        List<RoomMember> roomMemberList = loggedInMember.getRoomMemberList();
        List<Long> loggedInGroupRoomNumber = roomMemberList.stream().map(roomMember ->
                roomMember.getRoom().getNumber())
                .collect(Collectors.toList());

        //로그인 사용자가 소속된 챌린지 개인룸
        List<Room> singleRooms = loggedInMember.getSingleRooms();
        List<Long> loggedInSingleRoomNumber = singleRooms.stream().map(singleRoom ->
                        singleRoom.getNumber())
                .collect(Collectors.toList());

        //파라미터로 사용자가 소속된 룸 넘버 전달, 해당 룸넘버와 연결되는 다짐들 리턴
        List<Dajim> dajims = dajimFeedRepository.findAllByMemberAndOpen(loggedInGroupRoomNumber, loggedInSingleRoomNumber);

        if (dajims==null){
            throw new RuntimeException("다짐을 확인할 수 없습니다."); //임시 RuntimeException
        }

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
