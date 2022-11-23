package challenge.nDaysChallenge.controller.dajim;

<<<<<<< HEAD
public class DajimController {
=======

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

    //다짐 업로드, 수정
    @PostMapping(name = "/challenge/{challengeId}/upload")
    public ResponseEntity<?> uploadDajim(@PathVariable("challengeId") Long roomNumber,
                                         @RequestBody DajimRequestDto dajimRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        checkLogin(userDetailsImpl);
        Dajim dajim = dajimService.uploadDajim(roomNumber, dajimRequestDto, userDetailsImpl);
        DajimResponseDto savedDajim = new DajimResponseDto(dajim.getNumber(), dajim.getMember().getNickname(),dajim.getContent());

        return ResponseEntity.ok().body(savedDajim);
    }

    //다짐 조회
    @GetMapping(name = "/challenge/{challengeId}")
    public ResponseEntity<?> viewDajimOnChallenge(@PathVariable("challengeId") Long roomNumber,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        checkLogin(userDetailsImpl);
        List<Dajim> dajims = dajimService.viewDajimInRoom(roomNumber);
        List<DajimResponseDto> dajimsList = dajims.stream().map(dajim ->
                new DajimResponseDto(
                        dajim.getNumber(),
                        dajim.getMember().getNickname(),
                        dajim.getContent()))
                    .collect(Collectors.toList());

        return ResponseEntity.ok().body(dajimsList);
    }

    private void checkLogin(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl) {
        if (userDetailsImpl == null) {
            throw new RuntimeException("로그인한 멤버만 사용할 수 있습니다.");
        }
    }

>>>>>>> refs/remotes/origin/develop
}
