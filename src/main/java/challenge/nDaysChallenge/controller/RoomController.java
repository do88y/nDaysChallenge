package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.RoomRequestDTO;
import challenge.nDaysChallenge.dto.response.MemberResponseDto;
import challenge.nDaysChallenge.dto.response.RoomResponseDto;
import challenge.nDaysChallenge.security.SecurityUtil;
import challenge.nDaysChallenge.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    //챌린지 생성
    @PostMapping("/challenge/create")
    public ResponseEntity<?> createRoom(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                        @RequestBody RoomRequestDTO roomRequestDTO) {

        Room room = roomService.createRoom(memberAdapter.getMember(), roomRequestDTO);

        RoomResponseDto savedRoom = RoomResponseDto.builder()
                .name(room.getName())
                .category(room.getCategory().name())
                .reward(room.getReward())
                .type(room.getType().name())
                .status(room.getStatus().name())
                .passCount(room.getPassCount())
                .totalDays(room.getPeriod().getTotalDays())
                .startDate(room.getPeriod().getStartDate())
                .endDate(room.getPeriod().getEndDate())
                .reward(room.getReward())
                .groupMembers(roomRequestDTO.getGroupMembers())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    //챌린지 삭제
    @DeleteMapping("/challenge/{challengeId}")
    public ResponseEntity<?> deleteRoom(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                        @PathVariable("challengeId") Long roomNumber) {

        roomService.deleteRoom(memberAdapter.getMember(), roomNumber);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    //마이페이지 - 완료챌린지 전체 조회
    @GetMapping("/user/finished-challenges")
    public ResponseEntity<?> viewFinishedChallenges(@AuthenticationPrincipal MemberAdapter memberAdapter) {
        Member member = memberAdapter.getMember();


        return ResponseEntity.ok(null);
    }

}
