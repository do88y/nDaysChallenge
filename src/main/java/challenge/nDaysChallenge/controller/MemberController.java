package challenge.nDaysChallenge.controller;


import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.dto.response.MemberResponseDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.security.SecurityUtil;
import challenge.nDaysChallenge.service.MemberService;
import challenge.nDaysChallenge.service.RelationshipService;
import challenge.nDaysChallenge.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemberController { //마이페이지 전용

    private final MemberService memberService;
    private final RelationshipService relationshipService;

    @GetMapping("/user")
    public ResponseEntity<?> findMemberInfoById() {
        MemberResponseDto memberResponseDto = memberService.findMemberInfoById(SecurityUtil.getCurrentMemberId());

        return ResponseEntity.ok(memberResponseDto);
    }


    //친구 리스트
    @GetMapping("/friends")
    public ResponseEntity<?> findFriendList(@AuthenticationPrincipal User user) {

        List<Relationship> friends = relationshipService.findFriends(user);

        //릴레이션십리스폰스dto로 데이터 전달

        return ResponseEntity.status(HttpStatus.OK).body(friends);
    }
}
