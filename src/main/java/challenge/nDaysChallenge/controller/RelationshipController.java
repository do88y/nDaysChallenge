package challenge.nDaysChallenge.controller;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.dto.request.FindFriendsRequestDTO;
import challenge.nDaysChallenge.dto.request.relationship.RelationshipRequestDTO;
import challenge.nDaysChallenge.dto.response.relationship.AcceptResponseDTO;
import challenge.nDaysChallenge.dto.response.relationship.AskResponseDTO;
import challenge.nDaysChallenge.dto.response.FindFriendsResponseDTO;
import challenge.nDaysChallenge.repository.RelationshipRepository;
import challenge.nDaysChallenge.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RelationshipController {

    private final RelationshipService relationshipService;
    private final RelationshipRepository relationshipRepository;

    //닉네임, 아이디로 검색//
    @GetMapping("/friends/find")
    public ResponseEntity<?> findFriends(Principal principal,
                                         @RequestParam(value = "id", required = false, defaultValue = "") String id,
                                         @RequestParam(value = "nickname", required = false, defaultValue = "") String nickname) {
        Member foundFriend = relationshipService.findFriends(id, nickname);

        FindFriendsResponseDTO foundFriendDTO = new FindFriendsResponseDTO(
                foundFriend.getId(),
                foundFriend.getNickname(),
                foundFriend.getImage());

        return ResponseEntity.ok().body(foundFriendDTO);
    }


    //친구 요청 post//
    @PostMapping("/friends/request")
    public ResponseEntity<?> postViewRequestFriend(Principal principal,
                                               @RequestBody RelationshipRequestDTO applyDTO) {

        List<AskResponseDTO> savedRequestFriendsList = relationshipService.saveRelationship(principal.getName(),applyDTO);

        return ResponseEntity.ok().body(savedRequestFriendsList);
    }

    //친구 요청 get//
    @GetMapping("/friends/request")
    public ResponseEntity<?> getViewRequestFriend(Principal principal){
        //바로 repository 꺼 쓰기//
        List<Relationship> savedRequestFriendsList = relationshipRepository.findRelationshipByFriendAndStatus(principal.getName());
        List<AskResponseDTO> askResponseDTOList =RelationshipService.createResponseDTO(savedRequestFriendsList);

        return ResponseEntity.ok().body(askResponseDTOList);
    }

    //친구 수락==친구목록 //
    @PostMapping("/friends/accept")
    public ResponseEntity<?> acceptFriendStatus(Principal principal, @RequestBody RelationshipRequestDTO applyDTO) {
        List<AcceptResponseDTO> acceptRelationship = relationshipService.acceptRelationship(principal.getName(), applyDTO);

        return ResponseEntity.ok().body(acceptRelationship);

    }


    @GetMapping("/friends/accept")
    public ResponseEntity<?> getAcceptFriendStatus(Principal principal){
        //바로 repository 꺼 쓰기//
        List<Relationship> acceptRelationship = relationshipRepository.findRelationshipByUserAndStatus(principal.getName());
        List<AcceptResponseDTO> acceptResponseDTOList = new ArrayList<>();

        for (Relationship relationship : acceptRelationship) {
            AcceptResponseDTO acceptFollowerDTO = AcceptResponseDTO.builder()
                    .id(relationship.getFriend().getId())
                    .nickname(relationship.getFriend().getNickname())
                    .image(relationship.getFriend().getImage())
                    .relationshipStatus(relationship.getStatus().name())
                    .build();

            acceptResponseDTOList.add(acceptFollowerDTO);
        }
        return ResponseEntity.ok().body(acceptResponseDTOList);
    }


    //요청거절 relationship 객채 삭제//
    @DeleteMapping("/friends/request")
    public ResponseEntity<?> deleteFriendStatus(Principal principal, @RequestBody RelationshipRequestDTO applyDTO ) {
        List<AcceptResponseDTO> friendList = relationshipService.deleteEachRelation(principal.getName(), applyDTO);

        return ResponseEntity.ok().body(friendList);
    }
}