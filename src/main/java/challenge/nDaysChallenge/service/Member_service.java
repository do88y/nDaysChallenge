package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class Member_service {

    private final MemberRepository member_repository;
    private final RoomMemberRepository roomMemberRepository;

    //==회원가입==//



    private void validateDuplicateEmail(Member member){

    }


    /**
     * roomCount +1
     */
    public void addCount(Member member) {
        findOne(member).add();
    }

    /**
     * roomCount -1
     */
    public void reduceCount(Member member) {
        findOne(member).reduce();
    }

    //MemberRoom에서 회원번호로 챌린지 만든 회원 조회
    public RoomMember findOne(Member member) {
        return roomMemberRepository.findByMemberNumber(member.getNumber());
    }

}
