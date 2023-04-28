package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.dto.request.dajim.DajimUpdateRequestDto;
import challenge.nDaysChallenge.dto.request.dajim.DajimUploadRequestDto;
import challenge.nDaysChallenge.dto.response.dajim.DajimFeedResponseDto;
import challenge.nDaysChallenge.dto.response.dajim.DajimResponseDto;
import challenge.nDaysChallenge.service.dajim.CustomSliceImpl;
import challenge.nDaysChallenge.service.dajim.DajimService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class DajimController {

    private final DajimService dajimService;

    //다짐 등록
    @PostMapping("/challenge/{challengeId}/dajim")
    public ResponseEntity<?> uploadDajim(@PathVariable("challengeId") Long roomNumber,
                                         @RequestBody DajimUploadRequestDto dajimUploadRequestDto,
                                         Principal principal){
        DajimResponseDto dajimResponseDto = dajimService.uploadDajim(roomNumber, dajimUploadRequestDto, principal.getName());

        return ResponseEntity.ok().body(dajimResponseDto);
    }

    //다짐 수정
    @PatchMapping("/challenge/{challengeId}/dajim")
    public ResponseEntity<?> updateDajim(@PathVariable("challengeId") Long roomNumber,
                                         @RequestBody DajimUpdateRequestDto dajimUpdateRequestDto,
                                         Principal principal){
        DajimResponseDto dajimResponseDto = dajimService.updateDajim(roomNumber, dajimUpdateRequestDto, principal.getName());

        return ResponseEntity.ok().body(dajimResponseDto);
    }

    //챌린지룸에 다짐 불러오기
    @GetMapping("/challenge/{challengeId}/dajim")
    public ResponseEntity<?> viewDajimOnChallenge(@PathVariable("challengeId") Long roomNumber, Principal principal){
        List<DajimResponseDto> dajimResponseDtoList = dajimService.viewDajimInRoom(roomNumber);

        return ResponseEntity.ok().body(dajimResponseDtoList);
    }

}
