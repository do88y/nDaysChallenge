package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import challenge.nDaysChallenge.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RelationshipService {

    //리포지토리 값을 쓸꺼니까 먼저 선언을 해줌//
    private final RelationshipRepository relationshipRepository;

    //relationship 엔티티에 값을 넣는 메서드//
    public void friendRequest(Member userNumber, Member friendNumber){
        //빌더를 통해 생성자를 만들기//
        Relationship member = Relationship.builder()
                                            .userNumber(userNumber)
                                            .friendNumber(friendNumber)
                                            .build();

    }




    //클라이언트로 받은 값으로 상태를 업데이트 해주는 메서드//
    public RelationshipStatus updateStatus(RelationshipStatus status, Member userNumber) {
        Relationship findUser = relationshipRepository.findByUserNumber(userNumber.getNumber());//relationship 엔티티 정보를 다 가져왔으니 상태만 빼야해//
        RelationshipStatus friendStatus = findUser.getStatus();
        if (status == RelationshipStatus.ACCEPT) {
            friendStatus = RelationshipStatus.ACCEPT;
        }
        if (status == RelationshipStatus.REFUSE) {
            friendStatus = RelationshipStatus.REFUSE;
        }

        return friendStatus;
    }



    //리포지토리에서 친구 리스트 검색하는 메서드//
    public Relationship confirmFriends (Member user,RelationshipStatus status) {
        return relationshipRepository.findRelationshipByUserNumberAndStatus(user.getNumber(), status);
    }



}
