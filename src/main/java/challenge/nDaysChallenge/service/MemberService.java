package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //==회원가입==//
    public Member saveMember(Member member){
        validateDuplicateEmail(member);
        return memberRepository.saveId(member);
    }

    //==id(email)중복체크==//
    private void validateDuplicateEmail(Member member){
        Member findMember = memberRepository.findOneId(member.getNumber());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    //==닉네임==//
    public Member saveNickname(Member member){
        validateDuplicateNickname(member);
        return memberRepository.saveNickName(member);
    }

    //==닉네임 중복체크==//
    private void validateDuplicateNickname(Member member) {
        Member findNickname = memberRepository.findNickname(member.getNickname());
        if(findNickname != null){
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }





}
