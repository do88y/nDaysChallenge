package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.dajim.DajimRequestDto;
import challenge.nDaysChallenge.domain.Member;
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

    //다짐 등록 및 수정
    public DajimResponseDto uploadDajim(Long roomNumber, DajimRequestDto dajimRequestDto, Member member) {
        Dajim savedDajim;

        if (dajimRequestDto.getDajimNumber()==null){ //requestDto에 다짐번호가 없으면 새로 등록

            Room room = dajimRepository.findByRoomNumber(roomNumber)
                    .orElseThrow(()
                            -> new RuntimeException("현재 챌린지룸 정보를 찾을 수 없습니다."));

            Dajim newDajim = dajimRequestDto.toDajim(room, member, dajimRequestDto);

            checkDajimRoomUser(newDajim, room, member);

            savedDajim = dajimRepository.save(newDajim);

        } else { //requestDto에서 다짐번호 전달받으면 업데이트

            Dajim dajim = dajimRepository.findByDajimNumber(dajimRequestDto.getDajimNumber())
                    .orElseThrow(()->new RuntimeException("현재 다짐 정보를 찾을 수 없습니다."));

            checkDajimUser(dajim,member);

            savedDajim = dajim.update(Open.valueOf(dajimRequestDto.getOpen()), dajimRequestDto.getContent());

        }

        if (savedDajim==null){
            throw new RuntimeException("다짐 작성에 실패했습니다.");
        }

        DajimResponseDto dajimResponseDto = DajimResponseDto.of(savedDajim);

        return dajimResponseDto;

    }

    //다짐 조회
    @Transactional(readOnly = true)
    public List<DajimResponseDto> viewDajimInRoom(Long roomNumber){
        List<Dajim> dajims = null;

        try {
            dajims = dajimRepository.findAllByRoomNumber(roomNumber);
        } catch (Exception e) {
            throw new RuntimeException("다짐을 확인할 수 없습니다."); //임시 RuntimeException
        }

        List<DajimResponseDto> dajimsList = dajims.stream().map(dajim ->
                        DajimResponseDto.of(dajim))
                .collect(Collectors.toList());

        return dajimsList;
    }

    private void checkDajimRoomUser(Dajim dajim, Room room, Member member){
        if (dajim.getRoom()!=room || dajim.getMember()!=member){
            throw new RuntimeException("다짐에 대한 권한이 없습니다.");
        }
    }

    private void checkDajimUser(Dajim dajim, Member member){
        if (dajim.getMember()!=member){
            throw new RuntimeException("다짐 작성자만 수정할 수 있습니다.");
        }
    }

}

