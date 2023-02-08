package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.dto.request.dajim.DajimUpdateRequestDto;
import challenge.nDaysChallenge.dto.request.dajim.DajimUploadRequestDto;
import challenge.nDaysChallenge.dto.response.dajim.DajimFeedResponseDto;
import challenge.nDaysChallenge.dto.response.dajim.DajimResponseDto;
import challenge.nDaysChallenge.service.dajim.DajimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class DajimController {

    private final DajimService dajimService;

    //다짐 등록
    @PostMapping("/challenge/{challengeId}/dajim")
    public ResponseEntity<?> uploadDajim(@PathVariable("challengeId") Long roomNumber,
                                         @RequestBody DajimUploadRequestDto dajimUploadRequestDto,
                                         @AuthenticationPrincipal MemberAdapter memberAdapter){
        checkLogin(memberAdapter.getMember());

        DajimResponseDto dajimResponseDto = dajimService.uploadDajim(roomNumber, dajimUploadRequestDto, memberAdapter.getMember());

        return ResponseEntity.ok().body(dajimResponseDto);
    }

    //다짐 수정
    @PatchMapping("/challenge/{challengeId}/dajim")
    public ResponseEntity<?> updateDajim(@PathVariable("challengeId") Long roomNumber,
                                        @RequestBody DajimUpdateRequestDto dajimUpdateRequestDto,
                                         @AuthenticationPrincipal MemberAdapter memberAdapter){
        checkLogin(memberAdapter.getMember());

        DajimResponseDto dajimResponseDto = dajimService.updateDajim(roomNumber, dajimUpdateRequestDto, memberAdapter.getMember());

        return ResponseEntity.ok().body(dajimResponseDto);
    }

    //챌린지룸에 다짐 불러오기
    @GetMapping("/challenge/{challengeId}/dajim")
    public ResponseEntity<?> viewDajimOnChallenge(@PathVariable("challengeId") Long roomNumber,
                                                  @AuthenticationPrincipal MemberAdapter memberAdapter){
        checkLogin(memberAdapter.getMember());

        List<DajimResponseDto> dajimResponseDtoList = dajimService.viewDajimInRoom(roomNumber);

        return ResponseEntity.ok().body(dajimResponseDtoList);
    }

    //피드 전체 조회 (다짐 + 감정스티커 리스트)
    @GetMapping("/feed")
    public ResponseEntity<?> viewDajimOnFeed(Principal principal){

        List<DajimFeedResponseDto> dajimFeedDto = dajimService.viewDajimOnFeed(principal);

        return ResponseEntity.ok().body(dajimFeedDto);

    }

    private void checkLogin(Member member) {
        if (member == null) {
            throw new RuntimeException("로그인한 멤버만 사용할 수 있습니다.");
        }
    }

}
