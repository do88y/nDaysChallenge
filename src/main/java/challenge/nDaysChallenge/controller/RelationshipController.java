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
    public ResponseEntity<?> findFriends(@AuthenticationPrincipal MemberAdapter memberAdapter, @RequestParam FindFriendsRequestDTO findFriendsRequestDTO) {
        //로그인 확인
        if (memberAdapter == null) {
            throw new RuntimeException("로그인한 멤버만 사용할 수 있습니다.");
        }

        Member foundFriend = relationshipService.findFriends(findFriendsRequestDTO.getId(), findFriendsRequestDTO.getNickname());

        FindFriendsResponseDTO foundFriendDTO = new FindFriendsResponseDTO(
                foundFriend.getId(),
                foundFriend.getNickname(),
                foundFriend.getImage());

        return ResponseEntity.ok().body(foundFriendDTO);
    }


    //친구 요청 post//
    @PostMapping("/friends/request")
    public ResponseEntity<?> postViewRequestFriend(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                               @RequestBody RelationshipRequestDTO applyDTO) {

        List<AskResponseDTO> savedRequestFriendsList = relationshipService.saveRelationship(memberAdapter.getMember(),applyDTO);

        return ResponseEntity.ok().body(savedRequestFriendsList);
    }

    //친구 요청 get//
    @GetMapping("/friends/request")
    public ResponseEntity<?> getViewRequestFriend(@AuthenticationPrincipal MemberAdapter memberAdapter){
        //바로 repository 꺼 쓰기//
        List<Relationship> savedRequestFriendsList = relationshipRepository.findRelationshipByFriendAndStatus(memberAdapter.getMember());
        List<AskResponseDTO> askResponseDTOList =RelationshipService.createResponseDTO(savedRequestFriendsList);

        return ResponseEntity.ok().body(askResponseDTOList);
    }

    //친구 수락==친구목록 //
    @PostMapping("/friends/accept")
    public ResponseEntity<?> acceptFriendStatus(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                                @RequestBody RelationshipRequestDTO applyDTO) {

        List<AcceptResponseDTO> acceptRelationship = relationshipService.acceptRelationship(memberAdapter.getMember(), applyDTO);

        return ResponseEntity.ok().body(acceptRelationship);

    }


    @GetMapping("/friends/accept")
    public ResponseEntity<?> getAcceptFriendStatus(@AuthenticationPrincipal MemberAdapter memberAdapter){
        //바로 repository 꺼 쓰기//
        List<Relationship> acceptRelationship = relationshipRepository.findRelationshipByUserAndStatus(memberAdapter.getMember());
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
    public ResponseEntity<?> deleteFriendStatus( @AuthenticationPrincipal MemberAdapter memberAdapter,
                                                 @RequestBody RelationshipRequestDTO applyDTO ) {

        List<AcceptResponseDTO> friendList = relationshipService.deleteEachRelation(memberAdapter.getMember(), applyDTO);

        return ResponseEntity.ok().body(friendList);
    }
}