package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import challenge.nDaysChallenge.dto.response.DajimResponseDto;
import challenge.nDaysChallenge.service.dajim.DajimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class DajimController {

    private final DajimService dajimService;

    //다짐 업로드 (등록 & 수정)
    @PostMapping("/challenge/{challengeId}")
    public ResponseEntity<?> uploadDajim(@PathVariable("challengeId") Long roomNumber,
                                         @RequestBody DajimRequestDto dajimRequestDto,
                                         @AuthenticationPrincipal MemberAdapter memberAdapter){
        checkLogin(memberAdapter.getMember());

        Dajim dajim = dajimService.uploadDajim(roomNumber, dajimRequestDto, memberAdapter.getMember());
        DajimResponseDto savedDajim = new DajimResponseDto(
                                        dajim.getNumber(),
                                        dajim.getMember().getNickname(),
                                        memberAdapter.getMember().getImage(),
                                        dajim.getContent(),
                                        dajim.getOpen().toString(),
                                        dajim.getUpdatedDate());

        if (savedDajim==null){
            throw new RuntimeException("다짐 작성에 실패했습니다.");
        }

        return ResponseEntity.ok().body(savedDajim);
    }

//    //다짐 수정 (다짐내용, 공개여부 리턴)
//    @PutMapping("/challenge/{challengeId}/{dajimId}")
//    public ResponseEntity<?> updateDajim(@PathVariable("dajimId") Long dajimNumber,
//                                         @RequestBody DajimRequestDto dajimRequestDto,
//                                         @AuthenticationPrincipal MemberAdapter memberAdapter){
//        checkLogin(memberAdapter.getMember());
//
//        Dajim updatedDajim = dajimService.updateDajim(dajimNumber, dajimRequestDto, memberAdapter.getMember());
//        DajimResponseDto newDajim = new DajimResponseDto(
//                                        updatedDajim.getNumber(),
//                                        updatedDajim.getMember().getNickname(),
//                                        memberAdapter.getMember().getImage(),
//                                        updatedDajim.getContent(),
//                                        updatedDajim.getOpen().toString(),
//                                        updatedDajim.getUpdatedDate());
//
//        if (newDajim==null){
//            throw new RuntimeException("다짐 작성에 실패했습니다.");
//        }
//
//        return ResponseEntity.ok().body(newDajim);
//    }

    //전체 다짐 조회
    @GetMapping("/challenge/{challengeId}")
    public ResponseEntity<?> viewDajimOnChallenge(@PathVariable("challengeId") Long roomNumber,
                                                  @AuthenticationPrincipal MemberAdapter memberAdapter){
        checkLogin(memberAdapter.getMember());

        List<Dajim> dajims = dajimService.viewDajimInRoom(roomNumber);
        List<DajimResponseDto> dajimsList = dajims.stream().map(dajim ->
                new DajimResponseDto(
                        dajim.getNumber(),
                        dajim.getMember().getNickname(),
                        memberAdapter.getMember().getImage(),
                        dajim.getContent(),
                        dajim.getOpen().toString(),
                        dajim.getUpdatedDate()))
                    .collect(Collectors.toList());

        return ResponseEntity.ok().body(dajimsList);
    }

    private void checkLogin(Member member) {
        if (member == null) {
            throw new RuntimeException("로그인한 멤버만 사용할 수 있습니다.");
        }
    }

}
