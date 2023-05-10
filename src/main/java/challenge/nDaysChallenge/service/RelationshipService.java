package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import challenge.nDaysChallenge.dto.request.relationship.RelationshipRequestDTO;
import challenge.nDaysChallenge.dto.response.relationship.AcceptResponseDTO;
import challenge.nDaysChallenge.dto.response.relationship.AskResponseDTO;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.relationship.RelationshipRepository;
import challenge.nDaysChallenge.repository.relationship.FriendSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RelationshipService {

    private final FriendSearchRepository friendSearchRepository;

    private final RelationshipRepository relationshipRepository;
    private final MemberRepository memberRepository;

    //id, nickname 검색//
    public Member findFriends(String id, String nickname) {

        if (!hasText(id)&&!hasText(nickname)){
            throw new RuntimeException("아이디 또는 닉네임을 입력해주세요.");
        }

        return friendSearchRepository.findByIdOrNickname(id, nickname);
    }

    //relationship 생성//
    @Transactional
    public  List<AskResponseDTO> saveRelationship(String id, RelationshipRequestDTO dto) {
        Member me = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 id의 사용자를 찾아오는 데 실패했습니다."));

        Optional<Member> findFriend = memberRepository.findById(dto.getId());
        Member friend = findFriend.orElseThrow(() -> new NoSuchElementException("해당 멤버가 존재하지않습니다."));

        Relationship userRelationship = Relationship.readyCreateRelation(me, friend);
        Relationship friendRelationship = Relationship.readyCreateRelation(friend, me);

        relationshipRepository.save(userRelationship);
        relationshipRepository.save(friendRelationship);

        //내 요청 리스트(내가 받은 요청 리스트) 보기//
        List<Relationship> viewRequestList = relationshipRepository.findRelationshipByFriendAndStatus(me.getId());
        List<AskResponseDTO> askResponseDTOList = createResponseDTO(viewRequestList);

        return askResponseDTOList;
    }


    //request response 따로 메서드로 만듦//
    public static List<AskResponseDTO> createResponseDTO(List<Relationship> viewRequestList) {
        List<AskResponseDTO> askResponseDTOList = new ArrayList<>();

        for (Relationship askRelation : viewRequestList) {
            AskResponseDTO askResponseDTO = AskResponseDTO.builder()
                    .id(askRelation.getFriend().getId())
                    .nickname(askRelation.getFriend().getNickname())
                    .image(askRelation.getFriend().getImage())
                    .requestDate(LocalDateTime.now())
                    .build();

                    askResponseDTOList.add(askResponseDTO);
        }
        return askResponseDTOList;
    }


    @Transactional
    //수락 버튼을 눌렀을 때 시행되는 메서드//
    public List<AcceptResponseDTO> acceptRelationship(String id, RelationshipRequestDTO applyDTO) {
        Member me = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 id의 사용자를 찾아오는 데 실패했습니다."));

        Optional<Member> getFriendId = memberRepository.findById(applyDTO.getId());
        Member friend = getFriendId.orElseThrow(() -> new NoSuchElementException("해당 멤버가 없습니다."));

        Relationship findUser = relationshipRepository.findByUserIdAndFriendId(me.getId(), friend.getId());//유저 조회
        Relationship findFriend = relationshipRepository.findByUserIdAndFriendId(friend.getId(), me.getId());

        findUser.updateStatus(RelationshipStatus.ACCEPT);
        findFriend.updateStatus(RelationshipStatus.ACCEPT);
        me.addFriendList(findFriend);//친구 수락되면 일단 나도 수락상태가 되니까 친구리스트로 들어감
        friend.addFriendList(findUser);

        //확정된 친구 리스트//
        List<Relationship> confirmList = relationshipRepository.findRelationshipByUserAndStatus(me.getId());
        List<AcceptResponseDTO> acceptResponseDTOList = new ArrayList<>();

        for (Relationship relationship : confirmList) {
            Member friendInfo = memberRepository.findById(relationship.getFriend().getId()).orElseThrow(() -> new RuntimeException("해당 멤버가 없습니다."));
            AcceptResponseDTO acceptFollowerDTO = AcceptResponseDTO.builder()
                    .id(friendInfo.getId())
                    .nickname(friendInfo.getNickname())
                    .image(friendInfo.getImage())
                    .relationshipStatus(relationship.getStatus().name())
                    .acceptedDate(relationship.getAcceptedDate())
                    .build();

                    acceptResponseDTOList.add(acceptFollowerDTO);
        }
        return acceptResponseDTOList;
    }

    //친구 요청 거절//
    @Transactional
    public List<AcceptResponseDTO> deleteEachRelation(String myId, RelationshipRequestDTO dto) {
        Optional<Member> findId = memberRepository.findById(dto.getId());

        Member friend = findId.orElseThrow(() -> new NoSuchElementException("해당 id가 없습니다."));

        //엔티티 찾기//
        Relationship findUser = relationshipRepository.findByUserIdAndFriendId(myId, friend.getId());
        Relationship findFriend = relationshipRepository.findByUserIdAndFriendId(friend.getId(), myId);
        //서로를 찾아 지움//
        relationshipRepository.delete(findUser);
        relationshipRepository.delete(findFriend);


        //user 의 현재 친구리스트(수락한 친구들) 갱신/조회//
        List<Relationship> friendList = relationshipRepository.findRelationshipByUserAndStatus(myId);
        List<AcceptResponseDTO> friendList2 = new ArrayList<>();

        for (Relationship relationship : friendList) {
            Member member = memberRepository.findByNumber(relationship.getFriend().getNumber()).orElseThrow(
                    () -> new NoSuchElementException("해당 회원이 없습니다.")
            );
            AcceptResponseDTO friendInfo = AcceptResponseDTO.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .image(member.getImage())
                    .relationshipStatus(relationship.getStatus().name())
                    .build();

            friendList2.add(friendInfo);
        }

        return friendList2;

    }

}

