package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import challenge.nDaysChallenge.dto.request.RelationshipRequestDTO;
import challenge.nDaysChallenge.repository.MemberRepository;
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
    private final MemberRepository memberRepository;


    //relationship 엔티티에 값을 넣는 메서드//
//    public static void friendRequest(Member user, Member friend){
//        //빌더를 통해 생성자를 만들기//
//        Relationship member = Relationship.builder()
//                .userNumber(user)
//                .friendNumber(friend)
//                .build();
//
//    }

    //클라이언트로 받은 값으로 상태를 업데이트 해주는 메서드(요청, 수락)//
    public RelationshipStatus updateFriendStatus(Member member, RelationshipRequestDTO dto) {
        Relationship findUser = relationshipRepository.findByUserNumber(member.getNumber(), dto.getId());//relationship 엔티티 정보를 다 가져왔으니 상태만 빼야해//
        RelationshipStatus friendStatus = findUser.getStatus();
        if (dto.getRelationshipStatus().equals(String.valueOf(RelationshipStatus.ACCEPT))) {
            friendStatus = RelationshipStatus.ACCEPT;
        }
        if (dto.getRelationshipStatus().equals(String.valueOf(RelationshipStatus.REFUSE))) {
            friendStatus = RelationshipStatus.REFUSE;

        }

        return friendStatus;


    }

    //리포지토리에서 친구 리스트 검색하는 메서드(리스트조회)//
    public Member findFriends (String id, String nickname){
        if((id == null)){
           return memberRepository.findById(id)
                   .orElseThrow(()->new RuntimeException("해당 아이디가 검색되지 않습니다."));

        }
        if((nickname==null)){
           return memberRepository.findByNickname(nickname)
                   .orElseThrow(()->new RuntimeException("해당 닉네임이 검색되지 않습니다."));
        }

        return (Member) relationshipRepository;
    }

}