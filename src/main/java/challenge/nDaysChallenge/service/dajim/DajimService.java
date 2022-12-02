package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.dto.response.DajimResponseDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DajimService {

    private final DajimRepository dajimRepository;

    private final MemberRepository memberRepository;

    //다짐 등록 및 수정
    public DajimResponseDto uploadDajim(Long roomNumber, DajimRequestDto requestDto, Member member) {
        Dajim savedDajim;

        if (requestDto.getDajimNumber()==null){ //requestDto에 다짐번호가 없으면 새로 등록

            Room room = dajimRepository.findByRoomNumber(roomNumber)
                    .orElseThrow(()
                            -> new RuntimeException("현재 챌린지룸 정보를 찾을 수 없습니다."));

            Dajim newDajim = Dajim.builder()
                    .room(room)
                    .member(member)
                    .content(requestDto.getContent())
                    .open(Open.valueOf(requestDto.getOpen()))
                    .build();

            checkDajimRoomUser(newDajim, room, member);

            savedDajim = dajimRepository.save(newDajim);

        } else { //requestDto에서 다짐번호 전달받으면 업데이트

            Dajim dajim = dajimRepository.findByDajimNumber(requestDto.getDajimNumber())
                    .orElseThrow(()->new RuntimeException("현재 다짐 정보를 찾을 수 없습니다."));

            checkDajimUser(dajim,member);

            savedDajim = dajim.update(Open.valueOf(requestDto.getOpen()), requestDto.getContent());

        }

        if (savedDajim==null){
            throw new RuntimeException("다짐 작성에 실패했습니다.");
        }

        DajimResponseDto dajimResponseDto = new DajimResponseDto(
                savedDajim.getNumber(),
                savedDajim.getMember().getNickname(),
                savedDajim.getMember().getImage(),
                savedDajim.getContent(),
                savedDajim.getOpen().toString(),
                savedDajim.getUpdatedDate());

        return dajimResponseDto;

    }

    //다짐 조회
    public List<DajimResponseDto> viewDajimInRoom(Long roomNumber){
        List<Dajim> dajims = null;

        try {
            dajims = dajimRepository.findAllByRoomNumber(roomNumber);
        } catch (Exception e) {
            throw new RuntimeException("다짐을 확인할 수 없습니다."); //임시 RuntimeException
        }

        List<DajimResponseDto> dajimsList = dajims.stream().map(dajim ->
                        new DajimResponseDto(
                                dajim.getNumber(),
                                dajim.getMember().getNickname(),
                                dajim.getMember().getImage(),
                                dajim.getContent(),
                                dajim.getOpen().toString(),
                                dajim.getUpdatedDate()))
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

