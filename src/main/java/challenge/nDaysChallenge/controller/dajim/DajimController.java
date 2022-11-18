package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import challenge.nDaysChallenge.dto.response.DajimResponseDto;
import challenge.nDaysChallenge.dto.response.MemberResponseDto;
import challenge.nDaysChallenge.security.UserDetailsImpl;
import challenge.nDaysChallenge.service.dajim.DajimService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class DajimController { ////dto->엔티티 전환 어떻게??어디서??

    private final DajimService dajimService;

    //다짐 업로드, 수정
    @GetMapping(name = "/challenge")
    public ResponseEntity<?> uploadDajim(@RequestBody DajimRequestDto dajimRequestDto,
                                         @AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        return ResponseEntity.ok(dajimService.uploadDajim(dajimRequestDto, userDetailsImpl));
    }


    //챌린지 상세화면 - 각 Room 다짐 조회
    @GetMapping(name = "/challenge/{roomMemberNumber}")
    public ResponseEntity<?> viewDajimOnChallenge(@PathVariable("roomMemberNumber") Long roomNumber,
                                                  @AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        return ResponseEntity.ok(dajimService.viewDajimOnChallenge(roomNumber, userDetailsImpl));
    }

    //피드 - 전체 다짐 리스트 조회
    @GetMapping(name = "/feed")
    public ResponseEntity<?> viewDajimOnFeed(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        return ResponseEntity.ok(dajimService.viewDajimOnFeed(userDetailsImpl));
    }

}