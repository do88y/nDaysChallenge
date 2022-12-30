package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.dto.request.dajim.DajimRequestDto;
import challenge.nDaysChallenge.dto.response.dajim.DajimResponseDto;
import challenge.nDaysChallenge.service.dajim.DajimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DajimController {

    private final DajimService dajimService;

    //다짐 업로드 (등록 & 수정)
    @PostMapping("/challenge/{challengeId}/dajim")
    public ResponseEntity<?> uploadDajim(@PathVariable("challengeId") Long roomNumber,
                                         @RequestBody DajimRequestDto dajimRequestDto,
                                         @AuthenticationPrincipal MemberAdapter memberAdapter){
        checkLogin(memberAdapter.getMember());

        DajimResponseDto dajimResponseDto = dajimService.uploadDajim(roomNumber, dajimRequestDto, memberAdapter.getMember());

        return ResponseEntity.ok().body(dajimResponseDto);
    }

    //다짐 조회 (챌린지룸 내)
    @GetMapping("/challenge/{challengeId}/dajim")
    public ResponseEntity<?> viewDajimOnChallenge(@PathVariable("challengeId") Long roomNumber,
                                                  @AuthenticationPrincipal MemberAdapter memberAdapter){
        checkLogin(memberAdapter.getMember());

        List<DajimResponseDto> dajimResponseDtoList = dajimService.viewDajimInRoom(roomNumber, memberAdapter.getMember());

        return ResponseEntity.ok().body(dajimResponseDtoList);
    }

    private void checkLogin(Member member) {
        if (member == null) {
            throw new RuntimeException("로그인한 멤버만 사용할 수 있습니다.");
        }
    }

}
