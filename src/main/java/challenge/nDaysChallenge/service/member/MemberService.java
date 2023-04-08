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
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberService {

    private final PasswordEncoder passwordEncoder;

    private final MemberRepository memberRepository;

    private final DajimRepository dajimRepository;

    private final RoomMemberRepository roomMemberRepository;

    private final StampRepository stampRepository;

    private final SingleRoomRepository singleRoomRepository;
    private final GroupRoomRepository groupRoomRepository;

    private final EntityManager em;


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

        //참조 엔티티들 불러오기
        List<Dajim> dajims = dajimRepository.findAllByMemberNickname(nickname).orElseGet(ArrayList::new);
        List<RoomMember> roomMembers = roomMemberRepository.findAllByMemberNickname(nickname).orElseGet(ArrayList::new);
        List<Stamp> stamps = stampRepository.findAllByMemberNickname(nickname).orElseGet(ArrayList::new);
        List<SingleRoom> singleRooms = singleRoomRepository.findAll(member).orElseGet(ArrayList::new);
        List<GroupRoom> groupRooms = groupRoomRepository.findAll(member).orElseGet(ArrayList::new);

        //룸 도메인 연관관계 끊기
        deleteConnection(member,roomMembers, stamps, singleRooms, groupRooms);

        //관련 엔티티 삭제
        dajimRepository.deleteAll(dajims); //다짐 -> 이모션 삭제
        roomMemberRepository.deleteAll(roomMembers);
        stampRepository.deleteAll(stamps);
        singleRoomRepository.deleteAll(singleRooms);

        //멤버 삭제
        memberRepository.delete(member);

        return nickname;
    }

    private void deleteConnection(Member member, List<RoomMember> roomMembers, List<Stamp> stamps, List<SingleRoom> singleRooms, List<GroupRoom> groupRooms){
        if (!roomMembers.isEmpty()){
            for (RoomMember roomMember:roomMembers) {
                roomMember.deleteConnection();
            }
        }

        if (!stamps.isEmpty()){
            for (Stamp stamp:stamps) {
                stamp.deleteConnection();
            }
        }

        if (!groupRooms.isEmpty()){
            for (GroupRoom groupRoom : groupRooms) {
                if (groupRoom.getMember().equals(member)) {
                    groupRoom.deleteHostConnection();
                } else {
                    groupRoom.deleteGuestConnection();
                }
            }
        }

        if (!singleRooms.isEmpty()){
            for (SingleRoom singleRoom:singleRooms){
                singleRoom.deleteConnection();
            }
        }

        em.flush();
        em.clear();
    }


}