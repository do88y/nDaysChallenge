package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.dajim.DajimUpdateRequestDto;
import challenge.nDaysChallenge.dto.request.dajim.DajimUploadRequestDto;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.dto.response.dajim.DajimResponseDto;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DajimService {

    private final DajimRepository dajimRepository;

    //다짐 등록
    public DajimResponseDto uploadDajim(Long roomNumber, DajimUploadRequestDto dajimUploadRequestDto, Member member) {
        Room room = dajimRepository.findByRoomNumber(roomNumber)
                .orElseThrow(()
                        -> new RuntimeException("챌린지룸을 찾을 수 없습니다."));

        Dajim newDajim = dajimUploadRequestDto.toDajim(room, member, dajimUploadRequestDto);

        Dajim savedDajim = dajimRepository.save(newDajim);

        if (savedDajim==null){
            throw new RuntimeException("다짐 작성에 실패했습니다.");
        }

        return DajimResponseDto.of(savedDajim);

    }

    //다짐 수정
    public DajimResponseDto updateDajim(Long roomNumber, DajimUpdateRequestDto dajimUpdateRequestDto, Member member) {
        Dajim dajim = dajimRepository.findByDajimNumber(dajimUpdateRequestDto.getDajimNumber())
                .orElseThrow(()->new RuntimeException("다짐을 찾을 수 없습니다."));

        checkRoomAndMember(dajim, member, roomNumber); //다짐이 소속된 룸에서 실행 중인지, 작성한 다짐을 수정하는지 확인

        Dajim updatedDajim = dajim.update(Open.valueOf(dajimUpdateRequestDto.getOpen()), dajimUpdateRequestDto.getContent());

        return DajimResponseDto.of(updatedDajim);
    }

    //다짐 조회 (룸에 소속된 멤버 확인은 챌린지 상세 조회 메소드에서)
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

    //다짐 수정 시 작성자/룸 체크
    private void checkRoomAndMember(Dajim dajim, Member member, Long roomNumber){
        if (dajim.getMember()!=member || dajim.getRoom().getNumber()!=roomNumber){
            throw new RuntimeException("접근 권한이 없습니다.");
        }
    }

}

