package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import challenge.nDaysChallenge.dto.request.RelationshipRequestDTO;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RelationshipService {

    //리포지토리 값을 쓸꺼니까 먼저 선언을 해줌//
    private final RelationshipRepository relationshipRepository;
    private final MemberRepository memberRepository;

    //클라이언트로 받은 값으로 상태를 업데이트 후 프렌드 리스트로 들어가는 메서드(요청 받은거 수락하는 메서드)//
    public RelationshipStatus updateFriendStatus(Member member, RelationshipRequestDTO dto) {
        //나의 status = member//
        Relationship user =relationshipRepository.findByUser(member);   //user 찾기

        Member friend=memberRepository.findById(dto.getId()
        ).orElseThrow(()->new RuntimeException("아이디에 해당하는 친구를 찾을 수 없습니다."));    //친구아이디

        return null;
    }

    //생성자호출//
    public Relationship addRelationship (Member user, Member friend, RelationshipRequestDTO relationshipRequestDTO){
        //변수를 담아야해/
        Relationship relationship = Relationship.createRelationship(user, friend,relationshipRequestDTO.getRequestedDate(),relationshipRequestDTO.getAcceptedDate());

        return relationship;
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