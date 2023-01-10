package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import challenge.nDaysChallenge.dto.request.relationship.ApplyRequestDTO;
import challenge.nDaysChallenge.dto.response.relationship.AcceptResponseDTO;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.RelationshipRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RelationshipService {

    //리포지토리 값을 쓸꺼니까 먼저 선언을 해줌//
    private final RelationshipRepository relationshipRepository;
    private final MemberRepository memberRepository;


    //relationship 생성//
    @Transactional
    public void saveRelationship(Member user, ApplyRequestDTO dto) {

        Optional<Member> findFriend = memberRepository.findById(dto.getId());
        Member friend = findFriend.orElseThrow(() -> new NoSuchElementException("해당 멤버가 존재하지않습니다."));


        Relationship userRelationship = Relationship.readyCreateRelation(user, friend);
        Relationship friendRelationship = Relationship.readyCreateRelation(friend, user);
        relationshipRepository.save(userRelationship);
        relationshipRepository.save(friendRelationship);
    }


    //수락 버튼을 눌렀을 때 시행되는 메서드//
    public AcceptResponseDTO acceptRelationship(Member user, ApplyRequestDTO applyDTO) {
        Optional<Member> getFriendId = memberRepository.findById(applyDTO.getId());
        Member friend = getFriendId.orElseThrow(() -> new NoSuchElementException("해당 멤버가 없습니다."));

        Relationship findUser = relationshipRepository.findByUserAndFriend(user, friend);//유저 조회
        Relationship findFriend = relationshipRepository.findByUserAndFriend(friend, user);
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
                .relationshipStatus(findFriend.getStatus().name())
                .build();

        return acceptFollowerDTO;
//리턴값이 뷰에 전해져야하니까 responseDTO의 빌더로 값이 가서 친구의 정보로 들어가게
    }


    //친구 리스트//
//    @Transactional
//    public AcceptResponseDTO viewFriends(Member member, AcceptRequestDTO requestDTO) {
//
//    }


    //친구 요청 거절//
    @Transactional
    public List<AcceptResponseDTO> deleteEachRelation(Member user, ApplyRequestDTO dto) {
        Optional<Member> findId = memberRepository.findById(dto.getId());
        Member friend = findId.orElseThrow(() -> new NoSuchElementException("해당 id가 없습니다."));

        //거절을 눌렀을 때 시행되는 메서드//
        Relationship findUser = relationshipRepository.findByUserAndFriend(user, friend);
        Relationship findFriend = relationshipRepository.findByUserAndFriend(friend, user);
        //서로를 찾기//
        relationshipRepository.delete(findUser);
        relationshipRepository.delete(findFriend);

        //user의 현재 친구리스트(수락) 찾기//
        List<Relationship> friendList = relationshipRepository.findRelationshipByUserAndStatus(user, RelationshipStatus.ACCEPT);
        List<AcceptResponseDTO> friendList2 = new ArrayList<>();


        //친구 리스트 안에 있는 넘버로 멤버 리포지토리의 친구 id 발굴//
        for (Relationship relationship : friendList) {
            Member member = memberRepository.findByNumber(relationship.getFriend().getNumber()).orElseThrow(
                    () -> new NoSuchElementException("해당 회원이 없습니다.")
            );
            AcceptResponseDTO friendInfo = AcceptResponseDTO.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .image(member.getImage())
                    .relationshipStatus(RelationshipStatus.ACCEPT.name())
                    .build();

            friendList2.add(friendInfo);
        }

        return friendList2;

    }




    //리포지토리에서 친구 검색하는 메서드//
    public Member findFriends(String id, String nickname) {
        if ((id == null)) {
            return memberRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("해당 아이디가 검색되지 않습니다."));
        }

        if ((nickname == null)) {
            return memberRepository.findByNickname(nickname)
                    .orElseThrow(() -> new RuntimeException("해당 닉네임이 검색되지 않습니다."));
        }

        return (Member) relationshipRepository;
    }
}

