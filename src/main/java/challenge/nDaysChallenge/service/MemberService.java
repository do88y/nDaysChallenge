package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.RoomMember;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.RoomStatus;
import challenge.nDaysChallenge.dto.request.MemberEditRequestDto;
import challenge.nDaysChallenge.dto.response.MemberInfoResponseDto;
import challenge.nDaysChallenge.dto.response.MemberResponseDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;

    //회원정보 조회 (수정 전)
    @Transactional(readOnly = true)
    public MemberInfoResponseDto findMemberInfo(String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(()->new RuntimeException("해당 id의 사용자를 찾아오는 데 실패했습니다."));

        MemberInfoResponseDto memberInfoResponseDto = MemberInfoResponseDto.of(member);

        return memberInfoResponseDto;
    }

    //회원정보 수정
    public MemberInfoResponseDto editMemberInfo(Member member, MemberEditRequestDto memberEditRequestDto) {
        Member updatedMember = member.update(memberEditRequestDto.getNickname(),
                memberEditRequestDto.getPw(),
                memberEditRequestDto.getImage());

        MemberInfoResponseDto updatedMemberInfoDto = MemberInfoResponseDto.of(updatedMember);

        return updatedMemberInfoDto;
    }

    //회원 삭제
    public String deleteMember(Member member) {
        memberRepository.delete(member);
        return member.getNickname();
    }

}



