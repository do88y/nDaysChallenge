package challenge.nDaysChallenge.service.dajim;

<<<<<<< HEAD
public class DajimService {
=======
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.dto.response.DajimResponseDto;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
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
        Room room = dajimRepository.findByRoomNumber(roomNumber);

        Dajim dajim = new Dajim();
        dajim.builder()
                .room(room)
                .member(userDetailsImpl.getMember())
                .content(requestDto.getContent())
                .build();

        Dajim savedDajim = dajimRepository.save(dajim);

        return savedDajim;
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

>>>>>>> refs/remotes/origin/develop
}
