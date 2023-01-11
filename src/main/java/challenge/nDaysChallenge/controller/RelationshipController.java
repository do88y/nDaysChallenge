package challenge.nDaysChallenge.controller;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.dto.request.FindFriendsRequestDTO;
import challenge.nDaysChallenge.dto.request.relationship.RelationshipRequestDTO;
import challenge.nDaysChallenge.dto.response.relationship.AcceptResponseDTO;
import challenge.nDaysChallenge.dto.response.relationship.AskResponseDTO;
import challenge.nDaysChallenge.dto.response.FindFriendsResponseDTO;
import challenge.nDaysChallenge.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class RelationshipController {

    private final RelationshipService relationshipService;


    //닉네임, 아이디로 검색//
    @GetMapping("/friends/find")
    public ResponseEntity<?> findFriends(@RequestBody FindFriendsRequestDTO findFriendsRequestDTO) {
        String id;
        String nickname;

        try {
            nickname = findFriendsRequestDTO.getNickname();

        } catch (Exception e) {
            throw new NoSuchElementException("닉네임을 입력하지 않았습니다.");
        }

        try {
           id = findFriendsRequestDTO.getId();

        } catch (Exception e) {
            throw new NoSuchElementException("아이디를 입력하지않았습니다.");
        }

        Member member = relationshipService.findFriends(id, nickname);

        FindFriendsResponseDTO foundFriend = new FindFriendsResponseDTO(
                member.getId(),
                member.getNickname(),
                member.getImage());

        return ResponseEntity.ok().body(foundFriend);
    }


    //친구 요청 //
    @PostMapping("/friends/request")
    public ResponseEntity<?> viewRequestFriend(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                               @RequestBody RelationshipRequestDTO applyDTO) {
        //서비스에 넘겨주기//
        Member saveFriend = relationshipService.saveRelationship(memberAdapter.getMember(), applyDTO);

        AskResponseDTO savedRequestFriendsList = AskResponseDTO.builder()
                .id(saveFriend.getId())
                .nickname(saveFriend.getNickname())
                .image(saveFriend.getImage())
                .requestDate(LocalDateTime.now())
                .relationshipStatus(RelationshipStatus.REQUEST.name())
                .build();

        return ResponseEntity.ok().body(savedRequestFriendsList);

    }

    //친구 수락==친구목록 //
    @PostMapping("/friends/accept")
    public ResponseEntity<?> acceptFriendStatus(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                                @RequestBody RelationshipRequestDTO applyDTO) {

        List<AcceptResponseDTO> acceptRelationship = relationshipService.acceptRelationship(memberAdapter.getMember(), applyDTO);

        return ResponseEntity.ok().body(acceptRelationship);

    }



    //요청거절 relationship 객채 삭제//
    @DeleteMapping("/friends/request")
    public ResponseEntity<?> deleteFriendStatus( @AuthenticationPrincipal MemberAdapter memberAdapter,
                                                 @RequestBody RelationshipRequestDTO applyDTO ) {

        List<AcceptResponseDTO> friendList = relationshipService.deleteEachRelation(memberAdapter.getMember(), applyDTO);

        return ResponseEntity.ok().body(friendList);
    }
}