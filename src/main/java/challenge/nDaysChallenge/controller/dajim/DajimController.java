package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import challenge.nDaysChallenge.dto.response.DajimResponseDto;
import challenge.nDaysChallenge.security.UserDetailsImpl;
import challenge.nDaysChallenge.service.dajim.DajimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class DajimController {

    private final DajimService dajimService;

    //다짐 업로드
    @PostMapping("/challenge/{challengeId}")
    public ResponseEntity<?> uploadDajim(@PathVariable("challengeId") Long roomNumber,
                                         @RequestBody DajimRequestDto dajimRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        checkLogin(userDetailsImpl);
        Dajim dajim = dajimService.uploadDajim(roomNumber, dajimRequestDto, userDetailsImpl);
        DajimResponseDto savedDajim = new DajimResponseDto(dajim.getNumber(), dajim.getMember().getNickname(),dajim.getContent(),dajim.getOpen(),dajim.getUpdatedDate());

        if (savedDajim==null){
            throw new RuntimeException("다짐 작성에 실패했습니다.");
        }

        return ResponseEntity.ok().body(savedDajim);
    }


    //다짐 수정 (다짐내용, 공개여부 리턴)
    @PutMapping("/challenge/{challengeId}/{dajimId}")
    public ResponseEntity<?> updateDajim(@PathVariable("dajimId") Long dajimNumber,
                                         @RequestBody DajimRequestDto dajimRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        checkLogin(userDetailsImpl);

        Dajim updatedDajim = dajimService.updateDajim(dajimNumber, dajimRequestDto, userDetailsImpl);
        DajimResponseDto newDajim = new DajimResponseDto(updatedDajim.getNumber(), updatedDajim.getMember().getNickname(),updatedDajim.getContent(),updatedDajim.getOpen(), updatedDajim.getUpdatedDate());

        if (newDajim==null){
            throw new RuntimeException("다짐 작성에 실패했습니다.");
        }

        return ResponseEntity.ok().body(newDajim);
    }

    //전체 다짐 조회
    @GetMapping("/challenge/{challengeId}")
    public ResponseEntity<?> viewDajimOnChallenge(@PathVariable("challengeId") Long roomNumber,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        checkLogin(userDetailsImpl);

        List<Dajim> dajims = dajimService.viewDajimInRoom(roomNumber);
        List<DajimResponseDto> dajimsList = dajims.stream().map(dajim ->
                new DajimResponseDto(
                        dajim.getNumber(),
                        dajim.getMember().getNickname(),
                        dajim.getContent(),
                        dajim.getOpen(),
                        dajim.getUpdatedDate()))
                    .collect(Collectors.toList());

        return ResponseEntity.ok().body(dajimsList);
    }

    private void checkLogin(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        if (userDetailsImpl == null) {
            throw new RuntimeException("로그인한 멤버만 사용할 수 있습니다.");
        }
    }

}
