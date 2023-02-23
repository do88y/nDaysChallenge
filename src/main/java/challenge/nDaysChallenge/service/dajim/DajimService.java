package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.dajim.Sticker;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.dajim.DajimUpdateRequestDto;
import challenge.nDaysChallenge.dto.request.dajim.DajimUploadRequestDto;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.dto.response.dajim.DajimFeedResponseDto;
import challenge.nDaysChallenge.dto.response.dajim.DajimResponseDto;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DajimService {

    private final DajimRepository dajimRepository;

    private final RoomRepository roomRepository;

    private final EmotionRepository emotionRepository;

    //다짐 등록
    public DajimResponseDto uploadDajim(Long roomNumber, DajimUploadRequestDto dajimUploadRequestDto, Member member) {
        //if문으로 다짐 존재 체크 (같은 룸-멤버에선 하나의 다짐만 허용)
        if (dajimRepository.findByMemberNumberAndRoomNumber(member.getNumber(), roomNumber).isPresent()){
            throw new RuntimeException("이미 존재하는 다짐입니다.");
        }

        Room room = roomRepository.findByNumber(roomNumber)
                .orElseThrow(() -> new RuntimeException("챌린지룸을 찾을 수 없습니다."));

        Dajim newDajim = dajimUploadRequestDto.toDajim(room, member, dajimUploadRequestDto.getContent(), dajimUploadRequestDto.getOpen());

        Dajim savedDajim = dajimRepository.save(newDajim);

        return DajimResponseDto.of(savedDajim);
    }

    //다짐 수정
    public DajimResponseDto updateDajim(Long roomNumber, DajimUpdateRequestDto dajimUpdateRequestDto, Member member) {
        Dajim dajim = dajimRepository.findByMemberNumberAndRoomNumber(member.getNumber(), roomNumber) //////멤버넘버, 룸넘버로 파라미터 수정
                .orElseThrow(()->new RuntimeException("다짐을 찾을 수 없습니다."));

        checkRoomAndMember(dajim, member, roomNumber); //다짐이 소속된 룸에서 실행 중인지, 작성한 다짐을 수정하는지 확인

        Dajim updatedDajim = dajim.update(Open.valueOf(dajimUpdateRequestDto.getOpen()), dajimUpdateRequestDto.getContent());

        return DajimResponseDto.of(updatedDajim);
    }

    //다짐 조회 (챌린지 룸 내)
    @Transactional(readOnly = true)
    public List<DajimResponseDto> viewDajimInRoom(Long roomNumber){
        List<Dajim> dajims = dajimRepository.findAllByRoomNumber(roomNumber)
                    .orElseThrow(()-> new RuntimeException("다짐을 확인할 수 없습니다."));

        List<DajimResponseDto> dajimsList = dajims
                .stream()
                .map(dajim -> DajimResponseDto.of(dajim))
                .collect(Collectors.toList());

        return dajimsList;
    }

    //피드 - 전체 다짐 조회 (미로그인)
    @Transactional(readOnly = true)
    public Slice<DajimResponseDto> viewDajimFeedWithoutLogin(Pageable pageable) {
//        List<Dajim> dajims = dajimRepository.findAllByOpen()
//                .orElseGet(ArrayList::new);
//
//        //도메인->dto
//        List<DajimFeedResponseDto> dajimFeedList = dajims.stream().map(dajim ->
//                DajimFeedResponseDto.of(dajim, null)
//        ).collect(Collectors.toList());
        Slice<DajimResponseDto> dajimPage = dajimRepository.findByOpen(Open.PUBLIC, pageable)
                                                            .map(DajimResponseDto::of);

        return new CustomSliceImpl<>(dajimPage.getContent(),pageable, dajimPage.hasNext());
    }

    //피드 - 전체 다짐 조회 (로그인 시)
    @Transactional(readOnly = true)
    public Slice<DajimResponseDto> viewDajimFeedLoggedIn(Member member, Pageable pageable) {
//        List<Dajim> dajims = dajimRepository.findAllByOpen()
//                .orElseGet(ArrayList::new);
//
//        //도메인->dto
//        List<DajimFeedResponseDto> dajimFeedList = dajims.stream().map(dajim ->
//                DajimFeedResponseDto.of(dajim, member)
//        ).collect(Collectors.toList());

        Slice<DajimResponseDto> dajimPage = dajimRepository.findByOpen(Open.PUBLIC, pageable)
                                                            .map(DajimResponseDto::of);

        //allstickers, loginsticker 직접 추가 (slice.getcontent.add)


        return new CustomSliceImpl<>(dajimPage.getContent(),pageable, dajimPage.hasNext());
    }

    //다짐 수정 시 작성자/룸 체크
    private void checkRoomAndMember(Dajim dajim, Member member, Long roomNumber){
        if (dajim.getMember()!=member || dajim.getRoom().getNumber()!=roomNumber){
            throw new RuntimeException("해당 챌린지 룸에 대한 접근 권한이 없습니다.");
        }
    }

}

