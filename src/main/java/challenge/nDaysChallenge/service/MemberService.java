package challenge.nDaysChallenge.service;


import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberDto;


import challenge.nDaysChallenge.controller.dto.MemberResponseDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.security.SecurityUtil;

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
        
        return memberRepository.findByIdEquals(member.getId());

    }

    //==id(email)중복체크==//
    private void validateDuplicateEmail(Member member){
        Member findMember = memberRepository.findByNumber(member.getNumber());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    //==닉네임==//
    public Member saveNickname(Member memberNickname){
        validateDuplicateNickname(memberNickname);
        return memberRepository.findByNickname(memberNickname.getNickname());

    }

    //==닉네임 중복체크==//
    private void validateDuplicateNickname(Member member) {

        Member findNickname = memberRepository.findByNickname(member.getNickname());

        if(findNickname != null){
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }






    //입력한 아이디에 해당하는 사용자 정보 가져오기
    public MemberResponseDto getMemberInfo(String id){
        return memberRepository.findById(validateDuplicateEmail(id))
                .map(MemberResponseDto::of)
                .orElseThrow(()->new RuntimeException("해당 id의 유저 정보가 없습니다"));
    }

    //시큐리티컨텍스트 상 사용자 정보 가져오기
    public MemberResponseDto getMyInfo(){
        return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::of)
                .orElseThrow(()->new RuntimeException("로그인한 유저 정보가 없습니다"));
    }


}
