package challenge.nDaysChallenge.controller;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.dto.request.FindFriendsRequestDTO;
import challenge.nDaysChallenge.dto.request.relationship.ApplyRequestDTO;
import challenge.nDaysChallenge.dto.response.relationship.AcceptResponseDTO;
import challenge.nDaysChallenge.dto.response.relationship.RequestResponseDTO;
import challenge.nDaysChallenge.dto.response.FindFriendsResponseDTO;
import challenge.nDaysChallenge.repository.member.MemberRepository;
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
    private final MemberRepository memberRepository;


    //닉네임, 아이디로 검색//
    @GetMapping("/friends/find")
    public ResponseEntity<?> findFriends(@RequestBody FindFriendsRequestDTO findFriendsRequestDTO) {


        String nickname;
        try {
            nickname = findFriendsRequestDTO.getNickname();

        } catch (Exception e) {
            throw new NoSuchElementException("닉네임을 입력하지 않았습니다.");
        }

        String id;
        try {
            id = findFriendsRequestDTO.getId();
        } catch (Exception e) {
            throw new NoSuchElementException("아이디를 입력하지않았습니다.");
        }


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
        //서비스에 넘겨주기//
        relationshipService.saveRelationship(memberAdapter.getMember(),applyDTO);

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
    public ResponseEntity<?> acceptFriendStatus(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                                @RequestBody ApplyRequestDTO applyDTO) {

        AcceptResponseDTO acceptRelationship = relationshipService.acceptRelationship(memberAdapter.getMember(), applyDTO);

        return ResponseEntity.ok().body(acceptRelationship);

    }


    //친구 리스트//
//    @PostMapping("/friends/list")
//    public ResponseEntity<?> viewFriendList(@AuthenticationPrincipal MemberAdapter memberAdapter,
//                                                                   @RequestBody AcceptRequestDTO requestDTO){
//
//        AcceptResponseDTO friendList = relationshipService.viewFriends(memberAdapter.getMember(),requestDTO);
//        return ResponseEntity.ok().body(friendList);
//    }


    //요청거절 relationship 객채 삭제//
    @DeleteMapping("/friends/request")
    public ResponseEntity<?> deleteFriendStatus( @AuthenticationPrincipal MemberAdapter memberAdapter,
                                                 @RequestBody ApplyRequestDTO applyDTO ) {

        List<AcceptResponseDTO> friendList = relationshipService.deleteEachRelation(memberAdapter.getMember(), applyDTO);

        return ResponseEntity.ok().body(friendList);
    }
}