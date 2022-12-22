package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import challenge.nDaysChallenge.dto.request.relationship.ApplyRequestDTO;
import challenge.nDaysChallenge.dto.response.relationship.AcceptResponseDTO;
import challenge.nDaysChallenge.dto.response.relationship.AfterDeleteResponseDTO;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RelationshipService {

    //리포지토리 값을 쓸꺼니까 먼저 선언을 해줌//
    private final RelationshipRepository relationshipRepository;
    private final MemberRepository memberRepository;



    //relationship 생성//

    public void saveRelationship(@AuthenticationPrincipal MemberAdapter user, Member friend){

        Relationship userRelationship =Relationship.readyCreateRelation(user.getMember(),friend);
        Relationship friendRelationship =Relationship.readyCreateRelation(friend, user.getMember());
        relationshipRepository.save(userRelationship);
        relationshipRepository.save(friendRelationship);
    }



    public AcceptResponseDTO acceptRelationship (Member user, Member friend, ApplyRequestDTO applyDTO){
        //수락 버튼을 눌렀을 때 시행되는 메서드//
        Relationship findUser =relationshipRepository.findByUserAndFriend(user,friend);//유저 조회
        Relationship findFriend = relationshipRepository.findByUserAndFriend(friend,user);
        findUser.updateStatus(RelationshipStatus.ACCEPT);
        findFriend.updateStatus(RelationshipStatus.ACCEPT);
        user.addFriendList(findFriend);//친구 수락되면 일단 나도 수락상태가 되니까 친구리스트로 들어감
        friend.addFriendList(findUser);

        //response dto로 보내줄 값 생성자//
        AcceptResponseDTO acceptFollowerDTO = AcceptResponseDTO.builder()
                .id(friend.getId())
                .nickname(friend.getNickname())
                .image(friend.getImage())
                .acceptedDate(LocalDateTime.now())
                .relationshipStatus(applyDTO.getRelationshipStatus())
                .build();

        return acceptFollowerDTO;
//리턴값이 뷰에 전해져야하니까 responseDTO의 빌더로 값이 가서 친구의 정보로 들어가게
    }

    public AfterDeleteResponseDTO deleteEachRelation (Member user, Member friend){
        //거절을 눌렀을 때 시행되는 메서드//
        Relationship findUser =relationshipRepository.findByUserAndFriend(user,friend);
        Relationship findFriend = relationshipRepository.findByUserAndFriend(friend,user);
        //서로를 찾기//

        relationshipRepository.delete(findUser);
        relationshipRepository.delete(findFriend);

        //user의 현재 친구리스트(요청,수락) 찾기//
        List<Relationship> byUser = relationshipRepository.findByUser(user);

        //생성//
        AfterDeleteResponseDTO afterDeleteResponseDTO = new AfterDeleteResponseDTO(byUser);


        return afterDeleteResponseDTO;




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