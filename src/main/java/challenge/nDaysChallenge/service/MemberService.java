package challenge.nDaysChallenge.service;

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

    //입력한 아이디에 해당하는 사용자 정보 가져오기
    public MemberResponseDto getMemberInfo(String id){
        return memberRepository.findById(id)
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
