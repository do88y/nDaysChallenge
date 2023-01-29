package challenge.nDaysChallenge.service.member;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.room.RoomMember;
import challenge.nDaysChallenge.dto.request.member.MemberEditRequestDto;
import challenge.nDaysChallenge.dto.response.member.MemberInfoResponseDto;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.room.SingleRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    private final DajimRepository dajimRepository;

    private final RoomMemberRepository roomMemberRepository;

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
                passwordEncoder.encode(memberEditRequestDto.getPw()),
                memberEditRequestDto.getImage());

        MemberInfoResponseDto updatedMemberInfoDto = MemberInfoResponseDto.of(updatedMember);

        return updatedMemberInfoDto;
    }

    //회원 삭제
    public String deleteMember(Member member) {
        String nickname = member.getNickname();

        //constraintviolation 오류 해결 위해 fk 엔티티 삭제
        List<Dajim> dajims = dajimRepository.findAllByMemberNickname(nickname).orElseGet(ArrayList::new);
        List<RoomMember> roomMembers = roomMemberRepository.findAllByMemberNickname(nickname).orElseGet(ArrayList::new);

        if (!dajims.isEmpty()){
            dajimRepository.deleteAll(dajims);
        }

        if (!roomMembers.isEmpty()){
            roomMemberRepository.deleteAll(roomMembers); //룸멤버에서 탈퇴 회원만 삭제 (룸 유지)
        }

        //멤버 삭제
        memberRepository.delete(member);

        return nickname;
    }

}



