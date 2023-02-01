package challenge.nDaysChallenge.service.member;

import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.room.GroupRoom;
import challenge.nDaysChallenge.domain.room.RoomMember;
import challenge.nDaysChallenge.domain.room.SingleRoom;
import challenge.nDaysChallenge.dto.request.member.MemberEditRequestDto;
import challenge.nDaysChallenge.dto.response.member.MemberInfoResponseDto;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.StampRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.room.GroupRoomRepository;
import challenge.nDaysChallenge.repository.room.SingleRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class MemberService {

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    private final DajimRepository dajimRepository;

    private final RoomMemberRepository roomMemberRepository;

    private final StampRepository stampRepository;

    private final SingleRoomRepository singleRoomRepository;
    private final GroupRoomRepository groupRoomRepository;

    //아이디 중복 검사
    @Transactional(readOnly = true)
    public String idCheck(String id){
        boolean exists = memberRepository.existsById(id);

        if (exists){
            return "exists";
        } else {
            return "ok";
        }
    }

    //닉네임 중복 검사
    @Transactional(readOnly = true)
    public String nicknameCheck(String nickname){
        boolean exists = memberRepository.existsByNickname(nickname);

        if (exists){
            return "exists";
        } else {
            return "ok";
        }
    }

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
        List<Stamp> stamps = stampRepository.findAllByMemberNickname(nickname).orElseGet(ArrayList::new);
        List<SingleRoom> singleRooms = singleRoomRepository.findSingleRooms(member);
        List<GroupRoom> groupRooms = groupRoomRepository.findGroupRooms(member);

        //연관관계부터 삭제
        for (RoomMember roomMember:roomMembers){
            roomMember.deleteConnection();
        }

        for (Stamp stamp:stamps){
            stamp.deleteConnection();
        }

        for (GroupRoom groupRoom : groupRooms) {
            groupRoom.deleteConnection();
        }

        for (SingleRoom singleRoom:singleRooms){
            singleRoom.deleteConnection();
        }

        //레포지토리에서 직접 삭제
        if (!dajims.isEmpty()){
            dajimRepository.deleteAll(dajims); //탈퇴 회원 다짐 삭제 -> 이모션도 삭제
        }

        if (!roomMembers.isEmpty()){
            roomMemberRepository.deleteAll(roomMembers); //탈퇴 회원 룸멤버 테이블에서 삭제
        }

        if (!stamps.isEmpty()){
            stampRepository.deleteAll(stamps); //탈퇴 회원 스탬프 삭제
        }

        if (!singleRooms.isEmpty()){
            singleRoomRepository.deleteAll(singleRooms);
        }

        //멤버 삭제
        memberRepository.delete(member);

        return nickname;
    }

}