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


    //친구관계 저장 + 서로의 친구리스트에 들어가게//
    public AcceptResponseDTO saveAcceptRelation (Member user, Member friend, ApplyRequestDTO applyDTO){
        //상태가 REQUEST 에서 ACCEPT 가 되면 서로의 친구리스트에 들어가게//
            //서로를 일단 찾아//
        Relationship findUser = relationshipRepository.findByUserAndFriend(user, friend);
        Relationship findFriend = relationshipRepository.findByUserAndFriend(friend,user);

        if(findFriend==findUser){
            user.addFriendList(findFriend);
            friend.addFriendList(findUser);
        }

          //AcceptResponse dto 로 보내줄 값(친구의 정보) 들의 생성자//
        AcceptResponseDTO acceptFollowingDTO = AcceptResponseDTO.builder()
                .id(friend.getId())
                .nickname(friend.getNickname())
                .image(friend.getImage())
                .acceptedDate(LocalDateTime.now())
                .relationshipStatus(applyDTO.getRelationshipStatus())
                .friendsList(applyDTO.getFriendsList())
                .build();

        //친구관계 맺고 relationshipRepository 에 save 하기//
        Relationship userRelationship = Relationship.readyCreateRelation(user, friend);
        Relationship friendRelationship = Relationship.readyCreateRelation(friend,user);
        relationshipRepository.save(userRelationship);
        relationshipRepository.save(friendRelationship);

        return acceptFollowingDTO;
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