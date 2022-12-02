package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import challenge.nDaysChallenge.dto.request.relationship.ApplyRequestDTO;
import challenge.nDaysChallenge.dto.response.relationship.AcceptResponseDTO;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    //클라이언트로 받은 값으로 상태를 업데이트 후 프렌드 리스트로 들어가는 메서드(요청 수락하는 메서드)//
    public AcceptResponseDTO updateFriendStatus(Member member, Member friend, ApplyRequestDTO applyDTO) {
        //나의 status = member//

        //상태가 REQUEST에서 ACCEPT가 되면 서로의 친구리스트에 들어가게 하는 메서드//
        Relationship user =relationshipRepository.findByUser(member);      //유저 조회
        Relationship userUpdate = user.update(RelationshipStatus.ACCEPT);
        userUpdate.addFriendList(friend);      //친구 수락되면 일단 나도 수락상태가 되니까 친구리스트로 들어감

        Relationship myFriend = relationshipRepository.findByUser(friend);  //친구 조회
        Relationship friendUpdate = myFriend.update(RelationshipStatus.ACCEPT);
        friendUpdate.addFriendList(member);   //친구도 수락상태면 친구리스트에 들어가게

        //response dto로 보내줄 값 생성자//
        AcceptResponseDTO acceptFollowerDTO = AcceptResponseDTO.builder()
                .id(friend.getId())
                .nickname(friend.getNickname())
                .image(friend.getImage())
                .acceptedDate(LocalDateTime.now())
                .relationshipStatus(applyDTO.getRelationshipStatus())
                .friendsList(applyDTO.getFriendsList())
                .build();


        return acceptFollowerDTO;
        //리턴값이 뷰에 전해져야하니까 responseDTO 의 빌더로 값이 가서 친구의 정보로 들어가게
    }











    //리포지토리에서 친구 검색하는 메서드//
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