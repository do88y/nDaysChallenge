package challenge.nDaysChallenge.controller;


import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import challenge.nDaysChallenge.dto.request.FindFriendsRequestDTO;
import challenge.nDaysChallenge.dto.request.RelationshipRequestDTO;
import challenge.nDaysChallenge.dto.response.AcceptDTO;
import challenge.nDaysChallenge.dto.response.DeleteDTO;
import challenge.nDaysChallenge.dto.response.FindFriendsResponseDTO;
import challenge.nDaysChallenge.dto.response.RelationshipResponseDTO;
import challenge.nDaysChallenge.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RelationshipController {

    private final RelationshipService relationshipService;


    //친구 리스트//
    @GetMapping("/friends")
    public ResponseEntity<?> findFriends(@RequestBody FindFriendsRequestDTO findFriendsRequestDTO) {
        String nickname = findFriendsRequestDTO.getNickname();
        String id = findFriendsRequestDTO.getId();

        Member member = relationshipService.findFriends(id, nickname);
        FindFriendsResponseDTO foundFriend = new FindFriendsResponseDTO(
                member.getId(),
                member.getNickname());

        return ResponseEntity.ok().body(foundFriend);
    }


    //친구 요청 리스트 저장//
    @PostMapping("/friends/request")
    public ResponseEntity<?> updateFriendStatus(@AuthenticationPrincipal Member memberAdapter,
                                                                           @RequestBody RelationshipRequestDTO relationshipRequestDTO) {


        RelationshipStatus relationshipStatus = relationshipService.updateFriendStatus(memberAdapter,relationshipRequestDTO)
        RelationshipRequestDTO savedRequestFriendsList = RelationshipRequestDTO.builder()
                .id(relationshipRequestDTO.getId())
                .nickname(relationshipRequestDTO.getNickname())
                .image(relationshipRequestDTO.getImage())
                .requestedDate(relationshipRequestDTO.getRequestedDate())
                .acceptedDate(relationshipRequestDTO.getAcceptedDate())
                .relationshipStatus(relationshipRequestDTO.getRelationshipStatus())
                .build();


        return ResponseEntity.ok().body(savedRequestFriendsList);
    }


    //친구 수락 리스트 조회//
    @PostMapping("/friend/accept")
    public ResponseEntity<?> updateFriendStatus(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                                @RequestBody AcceptDTO acceptDTO) {

        RelationshipStatus relationshipStatus = relationshipService.updateFriendStatus(memberAdapter.getMember(), acceptDTO);
        AcceptDTO savedAcceptFriendsList = AcceptDTO.builder()
                .id(acceptDTO.getId())
                .nickname(acceptDTO.getNickname())
                .image(acceptDTO.getImage())
                .requestedDate(acceptDTO.getRequestedDate())
                .acceptedDate(acceptDTO.getAcceptedDate())
                .relationshipStatus(acceptDTO.getRelationshipStatus())
                .build();

        return ResponseEntity.ok().body(savedAcceptFriendsList);

    }

    //친구 삭제 //
    @DeleteMapping("/friends")
    public ResponseEntity<?> updateFriendStatus(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                                @RequestBody DeleteDTO deleteDTO) {

        RelationshipStatus relationshipStatus = relationshipService.updateFriendStatus(memberAdapter.getMember(), deleteDTO);
        RelationshipResponseDTO savedRefuseFriendsList = RelationshipResponseDTO.builder()
                .id(deleteDTO.getId())
                .nickname(deleteDTO.getNickname())
                .image(deleteDTO.getImage())
                .requestedDate(deleteDTO.getRequestedDate())
                .acceptedDate(deleteDTO.getAcceptedDate())
                .relationshipStatus(deleteDTO.getRelationshipStatus())
                .build();

        return ResponseEntity.ok().body(savedRefuseFriendsList);

    }
}




