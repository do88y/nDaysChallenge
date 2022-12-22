package challenge.nDaysChallenge.controller;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.dto.request.FindFriendsRequestDTO;
import challenge.nDaysChallenge.dto.request.relationship.ApplyRequestDTO;
import challenge.nDaysChallenge.dto.response.relationship.AcceptResponseDTO;
import challenge.nDaysChallenge.dto.response.relationship.AfterDeleteResponseDTO;
import challenge.nDaysChallenge.dto.response.relationship.RequestResponseDTO;
import challenge.nDaysChallenge.dto.response.FindFriendsResponseDTO;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class RelationshipController {

    private final RelationshipService relationshipService;
    private final MemberRepository memberRepository;


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


    //친구 요청 //
    @PostMapping("/friends/request")
    public ResponseEntity<?> viewRequestFriend(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                               @RequestBody ApplyRequestDTO applyDTO) {

        //request 에서 받은값을 response 에 보내줘야하니까//
        RequestResponseDTO savedRequestFriendsList = RequestResponseDTO.builder()
                .id(applyDTO.getId())
                .nickname(applyDTO.getNickname())
                .image(applyDTO.getImage())
                .requestDate(LocalDateTime.now())
                .relationshipStatus(applyDTO.getRelationshipStatus())
                .build();

        return ResponseEntity.ok().body(savedRequestFriendsList);

    }

    //친구 수락 //
    @PostMapping("/friends/accept")
    public ResponseEntity<?> acceptFriendStatus(@RequestBody ApplyRequestDTO applyDTO) {

        AcceptResponseDTO savedAcceptFriendsList = AcceptResponseDTO.builder()
                .id(applyDTO.getId())
                .nickname(applyDTO.getNickname())
                .image(applyDTO.getImage())
                .acceptedDate(LocalDateTime.now())
                .relationshipStatus(applyDTO.getRelationshipStatus())
                .build();

        return ResponseEntity.ok().body(savedAcceptFriendsList);

    }

    //요청거절 relationship 객채 삭제//
    @DeleteMapping("/friends/request")
    public ResponseEntity<?> deleteFriendStatus(    @AuthenticationPrincipal MemberAdapter memberAdapter,
                                                    @RequestBody ApplyRequestDTO applyDTO ) {

        Member user = memberAdapter.getMember();
        Member friend = memberRepository.findById(applyDTO.getId()).orElse(null);
        AfterDeleteResponseDTO DeleteResponseDTO = relationshipService.deleteEachRelation(user, friend);

        return ResponseEntity.ok().body(DeleteResponseDTO);
    }
}
