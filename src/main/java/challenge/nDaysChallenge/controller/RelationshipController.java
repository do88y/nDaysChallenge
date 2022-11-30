package challenge.nDaysChallenge.controller;


import challenge.nDaysChallenge.domain.RelationshipStatus;
import challenge.nDaysChallenge.dto.request.RelationshipRequestDTO;
import challenge.nDaysChallenge.dto.response.RelationshipResponseDTO;
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
                                                @RequestBody RelationshipRequestDTO relationshipRequestDTO, RelationshipResponseDTO relationshipResponseDTO) {


        //친구 요청 리스트 저장//
        RelationshipStatus relationshipStatus = relationshipService.updateFriendStatus(user, relationshipRequestDTO);
        RelationshipRequestDTO savedRequestFriendsList = RelationshipRequestDTO.builder()
                .id(relationshipRequestDTO.getId())
                .nickname(relationshipRequestDTO.getNickname())
                .image(relationshipRequestDTO.getImage())
                .requestedDate(relationshipRequestDTO.getRequestedDate())
                .acceptedDate(relationshipRequestDTO.getAcceptedDate())
                .relationshipStatus(String.valueOf(relationshipRequestDTO.getRelationshipStatus().equals(String.valueOf(RelationshipStatus.ACCEPT))))

        return ResponseEntity.status(HttpStatus.CREATED).body(savedRequestFriendsList);
    }
}

       ///친구 수락 리스트 저장//
//      RelationshipStatus relationshipStatus = relationshipService.updateFriendStatus(user,relationshipResponseDTO);
//        RelationshipResponseDTO savedResponseFriendsList = RelationshipResponseDTO.builder()
//                .id(relationshipResponseDTO.getId())
//                .nickname(relationshipResponseDTO.getNickname())
//                .image(relationshipResponseDTO.getImage())
//                .requestedDate(relationshipResponseDTO.getRequestedDate())
//                .acceptedDate(relationshipResponseDTO.getAcceptedDate())
//                .relationshipStatus(String.valueOf(relationshipResponseDTO.getRelationshipStatus().equals(String.valueOf(RelationshipStatus.ACCEPT))))
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(savedRequestFriendsList);
//    }




