package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.RoomRequestDTO;
import challenge.nDaysChallenge.dto.response.RoomResponseDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.security.UserDetailsImpl;
import challenge.nDaysChallenge.service.RelationshipService;
import challenge.nDaysChallenge.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;
    private final RelationshipService relationshipService;

    /**
     * 챌린지 생성
     */
    @PostMapping("/challenge/create")
    public ResponseEntity<?> createRoom(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl,
                                        @RequestBody RoomRequestDTO roomRequestDTO) {
        List<Relationship> friends = relationshipService.findFriends(userDetailsImpl);
        Member member = userDetailsImpl.getMember();

        Room room = roomService.createRoom(member, roomRequestDTO);
        RoomResponseDto savedRoom = RoomResponseDto.builder()
                .name(room.getName())
                .category(room.getCategory())
                .reward(room.getReward())
                .type(room.getType())
                .status(room.getStatus())
                .passCount(room.getPassCount())
                .totalDays(room.getPeriod().getTotalDays())
                .member(member)
                .friends((Member) friends)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }


    /**
     * 챌린지 삭제
     */
    @DeleteMapping("/challenge/{challengeId}")
    public ResponseEntity<?> deleteRoom(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                        @PathVariable("challengeId") Long roomNumber) {

        Long memberNumber = userDetails.getMember().getNumber();
        roomService.deleteRoom(memberNumber, roomNumber);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }
}
