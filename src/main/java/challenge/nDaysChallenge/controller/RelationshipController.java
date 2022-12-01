package challenge.nDaysChallenge.controller;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.dto.request.FindFriendsRequestDTO;
import challenge.nDaysChallenge.dto.request.RelationshipRequestDTO;
import challenge.nDaysChallenge.dto.response.AcceptDTO;
import challenge.nDaysChallenge.dto.response.FindFriendsResponseDTO;
import challenge.nDaysChallenge.dto.response.RelationshipResponseDTO;
import challenge.nDaysChallenge.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RelationshipController {

    private final RelationshipService relationshipService;


    //닉네임, 아이디로 검색//
    @GetMapping("/friends/find")
    public ResponseEntity<?> findFriends(@RequestBody FindFriendsRequestDTO findFriendsRequestDTO) {
        String nickname = findFriendsRequestDTO.getNickname();
        String id = findFriendsRequestDTO.getId();

        Member member = relationshipService.findFriends(id, nickname);
        FindFriendsResponseDTO foundFriend = new FindFriendsResponseDTO(
                member.getId(),
                member.getNickname());

        return ResponseEntity.ok().body(foundFriend);
    }


    //요청 리스트 //
    @PostMapping("/friends/request")
    public ResponseEntity<?> viewRequestFriendStatus(@RequestBody RelationshipRequestDTO relationshipRequestDTO) {

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


    //친구 수락 리스트//
    @PostMapping("/friends/accept")
    public ResponseEntity<?> acceptFriendStatus(@RequestBody RelationshipRequestDTO relationshipRequestDTO) {

        AcceptDTO savedAcceptFriendsList = AcceptDTO.builder()
                .id(relationshipRequestDTO.getId())
                .nickname(relationshipRequestDTO.getNickname())
                .image(relationshipRequestDTO.getImage())
                .requestedDate(relationshipRequestDTO.getRequestedDate())
                .acceptedDate(relationshipRequestDTO.getAcceptedDate())
                .relationshipStatus(relationshipRequestDTO.getRelationshipStatus())
                .build();

        return ResponseEntity.ok().body(savedAcceptFriendsList);

    }

    //요청거절 relationship 객채 삭제.//
    @DeleteMapping("/friends/request")
    public ResponseEntity<?> deleteFriendStatus(@RequestBody RelationshipRequestDTO relationshipRequestDTO) {

        RelationshipResponseDTO savedRefuseFriendsList = RelationshipResponseDTO.builder()
                .id(relationshipRequestDTO.getId())
                .nickname(relationshipRequestDTO.getNickname())
                .image(relationshipRequestDTO.getImage())
                .requestedDate(relationshipRequestDTO.getRequestedDate())
                .acceptedDate(relationshipRequestDTO.getAcceptedDate())
                .relationshipStatus(relationshipRequestDTO.getRelationshipStatus())
                .build();

        return ResponseEntity.ok().body(savedRefuseFriendsList);

    }

    //마이페이지 - 친구리스트 전체 조회
    @GetMapping("/user/friends")
    public ResponseEntity<?> viewFriendsList(@AuthenticationPrincipal MemberAdapter memberAdapter) {

        Member friends = relationshipService.findFriends(memberAdapter.getMember().getId(),memberAdapter.getMember().getNickname());

        //릴레이션십리스폰스dto로 데이터 전달

        return ResponseEntity.status(HttpStatus.OK).body(friends);
    }

}