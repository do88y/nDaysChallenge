package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.dto.response.DajimResponseDto;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DajimService {

    private final DajimRepository dajimRepository;

    //다짐 업로드
    public Dajim uploadDajim(Long roomNumber, DajimRequestDto requestDto, UserDetailsImpl userDetailsImpl) {
        Member member = userDetailsImpl.getMember();
        Room room = dajimRepository.findByRoomNumber(roomNumber)
                .orElseThrow(()-> new RuntimeException("현재 챌린지룸 정보를 찾을 수 없습니다."));

        Dajim newDajim = Dajim.builder()
                .room(room)
                .member(member)
                .content(requestDto.getContent())
                .open(Open.valueOf(requestDto.getOpen()))
                .build();

        checkDajimRoomUser(newDajim, room, userDetailsImpl);

        Dajim savedDajim = dajimRepository.save(newDajim);

        return savedDajim;

    }

    //다짐 수정
    public Dajim updateDajim(Long dajimNumber, DajimRequestDto requestDto, UserDetailsImpl userDetailsImpl){
        Dajim dajim = dajimRepository.findByDajimNumber(dajimNumber)
                .orElseThrow(()->new RuntimeException("현재 다짐 정보를 찾을 수 없습니다."));

        checkDajimUser(dajim,userDetailsImpl);

        Dajim updatedDajim = dajim.update(Open.valueOf(requestDto.getOpen()), requestDto.getContent());

        return updatedDajim;
    }



    //다짐 조회
    public List<Dajim> viewDajimInRoom(Long roomNumber){
        List<Dajim> dajims = null;

        try {
            dajims = dajimRepository.findAllByRoomNumber(roomNumber);
        } catch (Exception e) {
            throw new RuntimeException("다짐을 확인할 수 없습니다."); //임시 RuntimeException
        }

        return dajims;
    }

    private void checkDajimRoomUser(Dajim dajim, Room room, UserDetailsImpl userDetailsImpl){
        if (dajim.getRoom()!=room || dajim.getMember()!=userDetailsImpl.getMember()){
            throw new RuntimeException("다짐에 대한 권한이 없습니다.");
        }
    }

    private void checkDajimUser(Dajim dajim, UserDetailsImpl userDetailsImpl){
        if (dajim.getMember()!=userDetailsImpl.getMember()){
            throw new RuntimeException("다짐 작성자만 수정할 수 있습니다.");
        }
    }

}
