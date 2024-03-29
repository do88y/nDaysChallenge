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
import challenge.nDaysChallenge.exception.CustomError;
import challenge.nDaysChallenge.exception.CustomException;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final MemberRepository memberRepository;

    //다짐 등록
    public DajimResponseDto uploadDajim(Long roomNumber, DajimUploadRequestDto dajimUploadRequestDto, String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomError.USER_NOT_FOUND));

        //다짐 존재 여부 확인 (같은 룸-멤버에선 하나의 다짐만 허용)
        if (dajimRepository.findByMember_IdAndRoom_Number(id, roomNumber).isPresent()){
            throw new CustomException(CustomError.SPECIFIC_DAJIM_NOT_FOUND);
        }

        Room room = roomRepository.findByNumber(roomNumber)
                .orElseThrow(() -> new CustomException(CustomError.ROOM_NOT_FOUND));

        Dajim newDajim = dajimUploadRequestDto.toDajim(room, member, dajimUploadRequestDto.getContent(), dajimUploadRequestDto.getOpen());

        return DajimResponseDto.of(dajimRepository.save(newDajim));
    }

    //다짐 수정
    public DajimResponseDto updateDajim(Long roomNumber, DajimUpdateRequestDto dajimUpdateRequestDto, String id) {
        Dajim dajim = dajimRepository.findByMember_IdAndRoom_Number(id, roomNumber) //다짐이 소속된 룸에서 실행 중인지, 작성한 다짐을 수정하는지 확인
                .orElseThrow(() -> new CustomException(CustomError.SPECIFIC_DAJIM_NOT_FOUND));

        Dajim updatedDajim = dajim.update(Open.valueOf(dajimUpdateRequestDto.getOpen()), dajimUpdateRequestDto.getContent());

        return DajimResponseDto.of(updatedDajim);
    }

    //다짐 조회 (챌린지 룸 내)
    @Transactional(readOnly = true)
    public List<DajimResponseDto> viewDajimInRoom(Long roomNumber){
        List<Dajim> dajims = dajimRepository.findAllByRoomNumber(roomNumber)
                .orElseThrow(() -> new CustomException(CustomError.DAJIM_NOT_FOUND));

        return toDajimResponseDto(dajims);
    }

    //다짐 리스트 -> 다짐 응답 dto 리스트 변환
    private List<DajimResponseDto> toDajimResponseDto(List<Dajim> dajims){
        return dajims.stream()
                .map(dajim -> DajimResponseDto.of(dajim))
                .collect(Collectors.toList());
    }

}

