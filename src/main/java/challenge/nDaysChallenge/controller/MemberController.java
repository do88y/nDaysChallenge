package challenge.nDaysChallenge.controller;


import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.dto.request.member.MemberEditRequestDto;
import challenge.nDaysChallenge.dto.response.member.MemberInfoResponseDto;
import challenge.nDaysChallenge.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberController { //마이페이지 전용

    private final MemberService memberService;

    //회원정보 조회 (수정 전)
    @GetMapping("/user/details")
    public ResponseEntity<?> viewMemberInfo(@AuthenticationPrincipal MemberAdapter memberAdapter) {
        if (memberAdapter==null){
            throw new RuntimeException("@AuthenticationPrincipal 어노테이션 객체가 비었습니다");
        }

        log.info(memberAdapter.getMember().getId());
        log.info(memberAdapter.getMember().getNickname());
        log.info(memberAdapter.getMember().getPw());

        MemberInfoResponseDto memberInfoResponseDto = memberService.findMemberInfo(memberAdapter.getMember().getId());

        return ResponseEntity.ok().body(memberInfoResponseDto);
    }

    //회원정보 수정
    @PatchMapping("/user/edit")
    public ResponseEntity<MemberInfoResponseDto> editMemberInfo(@RequestBody MemberEditRequestDto memberEditRequestDto,
                                            @AuthenticationPrincipal MemberAdapter memberAdapter) {
        MemberInfoResponseDto memberInfoResponseDto =
                memberService.editMemberInfo(memberAdapter.getMember(), memberEditRequestDto);

        return ResponseEntity.ok().body(memberInfoResponseDto);
    }

    //회원 탈퇴
    @DeleteMapping("/user/withdrawal")
    public ResponseEntity<String> withdrawMember(@AuthenticationPrincipal MemberAdapter memberAdapter) {
        String nickname = memberService.deleteMember(memberAdapter.getMember());

        return ResponseEntity.ok().body(nickname+"님 탈퇴가 완료되었습니다."); //탈퇴한 회원 닉네임 리턴
    }

}
