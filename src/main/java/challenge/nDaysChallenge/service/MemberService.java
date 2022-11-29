package challenge.nDaysChallenge.service;
import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.dto.response.MemberResponseDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    //챌린지 갯수 조회 (memberDto에 담아보내기)
    public int getRoomCount(Member memberNumber) {
        return memberNumber.countRooms();
    }


    public MemberResponseDto findMemberInfoById(String id) {
        MemberResponseDto memberResponseDto = memberRepository.findById(id)
                .map(MemberResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인한 사용자 정보를 찾을 수 없습니다."));

        return memberResponseDto;
    }

}


