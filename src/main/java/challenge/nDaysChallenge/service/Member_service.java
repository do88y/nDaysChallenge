package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class Member_service {

    private final MemberRepository member_repository;

    //==회원가입==//



    private void validateDuplicateEmail(Member member){

    }

    /**
     * room +1
     */
    public void addRoom(Member memberNumber) {
        RoomMember roomMember = member_repository.findMemberRoom(memberNumber);
        roomMember.add();
    }

    /**
     * room -1
     */
    public void reduceRoom(Member memberNumber) {
        RoomMember roomMember = member_repository.findMemberRoom(memberNumber);
        roomMember.reduce();
    }

}
