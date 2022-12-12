package challenge.nDaysChallenge.controller;


import challenge.nDaysChallenge.domain.Member;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController { //마이페이지 전용

    private final MemberService memberService;


    //회원정보 조회
    @GetMapping("/user/details")
    public ResponseEntity<?> viewMemberInfo(
            @AuthenticationPrincipal
            UserDetails userDetails) {
//(expression = this == 'anonymousUser' ? null : member")
        if (userDetails==null){
            throw new RuntimeException("MemberAdapter 사용자를 불러올 수 없습니다.");
        }
        MemberInfoResponseDto memberInfoResponseDto = memberService.findMemberInfo(userDetails.getUsername());

        return ResponseEntity.ok().body(memberInfoResponseDto);
    }

    //회원정보 수정
    @PutMapping("/user/edit")
    public ResponseEntity<MemberInfoResponseDto> editMemberInfo(@RequestBody MemberEditRequestDto memberEditRequestDto,
                                            @AuthenticationPrincipal MemberAdapter memberAdapter) {
        MemberInfoResponseDto memberInfoResponseDto =
                memberService.editMemberInfo(memberAdapter.getMember(), memberEditRequestDto);

        return ResponseEntity.ok().body(memberInfoResponseDto);
    }

    //회원 탈퇴
    @PostMapping("/user/withdrawal")
    public ResponseEntity<String> withdrawMember(@AuthenticationPrincipal MemberAdapter memberAdapter) {
        String nickname = memberService.deleteMember(memberAdapter.getMember());

        return ResponseEntity.ok().body(nickname); //탈퇴한 회원 닉네임 리턴
    }

}
