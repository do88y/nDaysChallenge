package challenge.nDaysChallenge.controller;


import challenge.nDaysChallenge.domain.RelationshipStatus;
import challenge.nDaysChallenge.dto.request.RelationshipDTO;
import challenge.nDaysChallenge.service.RelationshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RelationshipController {

    private final RelationshipService relationshipService;

    @PostMapping("/friends")
    public ResponseEntity<?> updateFriendStatus(@AuthenticationPrincipal User user,
                                                @RequestBody RelationshipDTO relationshipDTO) {

        RelationshipStatus relationship = relationshipService.updateFriendStatus(user,relationshipDTO);
        RelationshipDTO savedFriendsList = RelationshipDTO.builder()
                .id(relationshipDTO.getId())
                .nickname(relationshipDTO.getNickname())
                .localDateTime(relationshipDTO.getLocalDateTime())
                .friendsList(relationshipDTO.getFriendsList())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(savedFriendsList);
    }
}



