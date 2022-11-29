package challenge.nDaysChallenge.controller;


import challenge.nDaysChallenge.dto.response.MemberResponseDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.security.SecurityUtil;
import challenge.nDaysChallenge.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/user")
    public ResponseEntity<?> findMemberInfoById(){
        MemberResponseDto memberResponseDto = memberService.findMemberInfoById(SecurityUtil.getCurrentMemberId());

        return ResponseEntity.ok(memberResponseDto);
    }

}
