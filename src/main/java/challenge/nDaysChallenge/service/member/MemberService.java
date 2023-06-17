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
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    //회원정보 조회 (수정 전)
    @Transactional(readOnly = true)
    public MemberInfoResponseDto findMemberInfo(String id) {
        Member member = validateMember(id); //회원 존재 확인

        return MemberInfoResponseDto.of(member);
    }

    //회원정보 수정
    @Transactional
    public MemberInfoResponseDto editMemberInfo(String id, MemberEditRequestDto memberEditRequestDto) {
        Member member = validateMember(id);

        validateNickname(id); //닉네임 중복 확인

        Member updatedMember = member.update(memberEditRequestDto, passwordEncoder);

        return MemberInfoResponseDto.of(updatedMember);
    }

    //회원 삭제
    @Transactional
    public String deleteMember(String id) {
        Member member = validateMember(id);

        //연관 엔티티 삭제
        deleteRelatedEntities(member);

        //멤버 삭제
        memberRepository.delete(member);

        return member.getNickname();
    }

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

    public Member validateMember(String id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 id의 사용자를 찾아오는 데 실패했습니다."));
    }

    public Member validateNickname(String id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 id의 사용자를 찾아오는 데 실패했습니다."));
    }

    @Transactional
    public void deleteRelatedEntities(Member member){
        //참조 엔티티들 불러오기
        List<Dajim> dajims = dajimRepository.findAllByMember_Nickname(member.getNickname()).orElseGet(ArrayList::new);
        List<RoomMember> roomMembers = roomMemberRepository.findAllByMemberNickname(member.getNickname()).orElseGet(ArrayList::new);
        List<Stamp> stamps = Stream
                .concat(singleRoomRepository.findStampByMember(member.getId()).stream(), roomMemberRepository.findStampByMember(member).stream())
                .collect(Collectors.toList());
        List<SingleRoom> singleRooms = singleRoomRepository.findAll(member.getId());
        List<GroupRoom> groupRooms = groupRoomRepository.findAll(member.getId());

        //룸 도메인 연관관계 끊기
        deleteConnection(member,roomMembers, stamps, singleRooms, groupRooms);

        em.flush();
        em.clear();

        //관련 엔티티 삭제
        dajimRepository.deleteAll(dajims); //다짐 -> 이모션 삭제
        roomMemberRepository.deleteAll(roomMembers);
        stampRepository.deleteAll(stamps);
        singleRoomRepository.deleteAll(singleRooms);
    }

    @Transactional
    public void deleteConnection(Member member, List<RoomMember> roomMembers, List<Stamp> stamps, List<SingleRoom> singleRooms, List<GroupRoom> groupRooms){

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
                if (member.equals(groupRoom.getMember())) {
                    groupRoom.deleteHostConnection();
                }
            }
        }

        if (!singleRooms.isEmpty()){
            for (SingleRoom singleRoom:singleRooms){
                singleRoom.deleteConnection();
            }
        }

    }

}