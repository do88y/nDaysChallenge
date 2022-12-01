package challenge.nDaysChallenge.controller;


import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.MemberEditRequestDto;
import challenge.nDaysChallenge.dto.response.MemberInfoResponseDto;
import challenge.nDaysChallenge.dto.response.MemberResponseDto;
import challenge.nDaysChallenge.security.SecurityUtil;
import challenge.nDaysChallenge.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class MemberController { //마이페이지 전용

    private final MemberService memberService;

    //회원정보 조회
    @GetMapping("/edit")
    public ResponseEntity<?> viewMemberInfo(@AuthenticationPrincipal MemberAdapter memberAdapter) {
        MemberInfoResponseDto memberInfoResponseDto = memberService.findMemberInfo(memberAdapter.getMember());

        return ResponseEntity.ok(memberInfoResponseDto);
    }

    //회원정보 수정 (+변경 닉네임 중복 검증)
    @PutMapping("/edit")
    public ResponseEntity<?> editMemberInfo(@RequestBody MemberEditRequestDto memberEditRequestDto,
                                            @AuthenticationPrincipal MemberAdapter memberAdapter) {
        MemberInfoResponseDto memberInfoResponseDto =
                memberService.editMemberInfo(memberAdapter.getMember(), memberEditRequestDto);

        return ResponseEntity.ok(memberInfoResponseDto);
    }

    //회원 탈퇴
    @PostMapping("/withdrawal")
    public ResponseEntity<?> withdrawMember(@AuthenticationPrincipal MemberAdapter memberAdapter) {
        String nickname = memberService.deleteMember(memberAdapter.getMember());

        return ResponseEntity.ok(nickname); //탈퇴한 회원 닉네임 리턴
    }

}
