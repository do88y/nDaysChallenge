package challenge.nDaysChallenge.controller;


import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.dto.request.MemberEditRequestDto;
import challenge.nDaysChallenge.dto.response.MemberInfoResponseDto;
import challenge.nDaysChallenge.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
public class MemberController { //마이페이지 전용

    private final MemberService memberService;

    //회원정보 조회
//    @GetMapping("/user/details")
//    public ResponseEntity<?> viewMemberInfo(@AuthenticationPrincipal UserDetails userDetails) {
//        String id = userDetails.getUsername();
//        MemberInfoResponseDto memberInfoResponseDto = memberService.findMemberInfo(id);
//
//        log.info(memberInfoResponseDto + " 받아오기 성공");
//
//        return ResponseEntity.ok().body(memberInfoResponseDto);
//    }

    @GetMapping("/user/details")
    public ResponseEntity<?> viewMemberInfo(@AuthenticationPrincipal MemberAdapter memberAdapter) {
        if (memberAdapter==null){
            throw new RuntimeException("@AuthenticationPrincipal 어노테이션 객체가 비었습니다");
        }
        MemberInfoResponseDto memberInfoResponseDto = memberService.findMemberInfo(memberAdapter.getMember().getId());

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
