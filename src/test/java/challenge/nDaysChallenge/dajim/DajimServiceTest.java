package challenge.nDaysChallenge.dajim;

import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.dajim.Sticker;
import challenge.nDaysChallenge.domain.member.Authority;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.dajim.DajimUploadRequestDto;
import challenge.nDaysChallenge.repository.RoomMemberRepository;
import challenge.nDaysChallenge.repository.dajim.DajimFeedRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DajimServiceTest {

    @Autowired
    DajimRepository dajimRepository;

    @Autowired
    DajimFeedRepository dajimFeedRepository;

    @Autowired
    EmotionRepository emotionRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomMemberRepository roomMemberRepository;

    @Autowired
    MemberRepository memberRepository;

    @DisplayName("다짐 저장")
    @Test
    void saveDajim(){
        //given
        Member member = new Member("user@naver.com","12345","userN",1, Authority.ROLE_USER);

        GroupRoom room = new GroupRoom(member, "newRoom",new Period(LocalDate.now(),100L), Category.ROUTINE,4,"보상", 0, 0);

        //when
        DajimUploadRequestDto dajimUploadRequestDto = new DajimUploadRequestDto("다짐 내용", "PUBLIC");
        Dajim newDajim = Dajim.builder()
                .room(room)
                .member(member)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build();
        Dajim savedDajim = dajimRepository.save(newDajim);

        checkDajimRoomUser(newDajim, room, member);

        //then
        assertThat(newDajim.getMember().getId()).isEqualTo(savedDajim.getMember().getId());
    }

    private static void checkDajimRoomUser(Dajim dajim, Room room, Member member){
        if (dajim.getRoom()!=room || dajim.getMember()!=member){
            throw new RuntimeException("다짐에 대한 권한이 없습니다.");
        }
    }

    @DisplayName("다짐 수정")
    @Test
    void modifyDajim(){
        //given
        Member member = new Member("user@naver.com","12345","userN",1, Authority.ROLE_USER);

        GroupRoom room = new GroupRoom(member, "newRoom",new Period(LocalDate.now(),100L), Category.ROUTINE,4,"보상", 0, 0);
        DajimUploadRequestDto dajimUploadRequestDto = new DajimUploadRequestDto("다짐 내용", "PUBLIC");
        Dajim dajim = Dajim.builder()
                .room(room)
                .member(member)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build();
        dajimRepository.save(dajim);

        //when
        Dajim newdajim = dajimRepository.findByNumber(1L).orElseThrow(()->new RuntimeException("현재 다짐을 찾을 수 없습니다."));
        Dajim updatedDajim = newdajim.update(Open.PRIVATE, "새 다짐");

        //then
        assertThat(updatedDajim.getContent()).isEqualTo("새 다짐");
    }

    @DisplayName("전체 룸멤버 다짐 조회")
    @Test
    void viewDajims(){
        //given
        Member member = new Member("user@naver.com","12345","userN",1, Authority.ROLE_USER);
        Member member2 = new Member("user2@naver.com","12345","userN2",1, Authority.ROLE_USER);
        GroupRoom room = new GroupRoom(member, "newRoom",new Period(LocalDate.now(),100L), Category.ROUTINE,4, "", 0, 0);

        DajimUploadRequestDto dajimUploadRequestDto = new DajimUploadRequestDto("다짐 내용", "PUBLIC");

        Dajim newDajim = Dajim.builder()
                .room(room)
                .member(member)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build();
        dajimRepository.save(newDajim);

        Dajim newDajim2 = Dajim.builder()
                .room(room)
                .member(member2)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build();
        dajimRepository.save(newDajim2);

        //when
        List<Dajim> dajimList = dajimRepository.findAllByRoomNumber(room.getNumber()).orElseThrow(()-> new RuntimeException("다짐을 확인할 수 없습니다."));

        //then
        assertThat(dajimList.size()).isEqualTo(2);
    }

    @DisplayName("피드 조회 - 다짐, 이모션")
    @Test
    @Transactional
    @Rollback(value = false)
    void viewFeed(){
        //given
        //멤버3 룸2
        Member member1 = Member.builder()
                .id("user@naver.com")
                .pw("12345")
                .nickname("userN")
                .image(1)
                .authority(Authority.ROLE_USER)
                .build();
        Member member2 = Member.builder()
                .id("user2@naver.com")
                .pw("12345")
                .nickname("userN2")
                .image(1)
                .authority(Authority.ROLE_USER)
                .build();
        Member member3 = Member.builder()
                .id("user3@naver.com")
                .pw("12345")
                .nickname("userN3")
                .image(1)
                .authority(Authority.ROLE_USER)
                .build();

        DajimUploadRequestDto dajimUploadRequestDto = new DajimUploadRequestDto("다짐 내용", "PUBLIC");

        //싱글룸 (룸1-멤버1)
        SingleRoom room1 = new SingleRoom("SingleRoom", new Period(LocalDate.now(), 10L), Category.ROUTINE, 2, "", 0, 0);
        member1.addSingleRooms(room1);
        roomRepository.save(room1);

        //싱글룸 다짐
        Dajim dajim = dajimRepository.save(Dajim.builder()
                .room(room1)
                .member(member1)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build());

        //그룹룸 (룸2-멤버1,2,3)
        GroupRoom room2 = new GroupRoom(member1, "GroupRoom", new Period(LocalDate.now(),100L), Category.ETC, 3, "", 0, 0);
        roomRepository.save(room2);
        RoomMember roomMember1 = RoomMember.createRoomMember(member1, room2, Stamp.createStamp(room2));
        RoomMember roomMember2 = RoomMember.createRoomMember(member2, room2, Stamp.createStamp(room2));
        RoomMember roomMember3 = RoomMember.createRoomMember(member3, room2, Stamp.createStamp(room2));
        roomMemberRepository.save(roomMember1);
        roomMemberRepository.save(roomMember2);
        roomMemberRepository.save(roomMember3);

        //그룹룸 다짐3
        Dajim dajim2 = dajimRepository.save(Dajim.builder()
                .room(room2)
                .member(member1)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build());
        Dajim dajim3 = dajimRepository.save(Dajim.builder()
                .room(room2)
                .member(member2)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf(dajimUploadRequestDto.getOpen()))
                .build());
        Dajim dajim4 = dajimRepository.save(Dajim.builder()
                .room(room2)
                .member(member3)
                .content(dajimUploadRequestDto.getContent())
                .open(Open.valueOf("PRIVATE"))
                .build());

        //그룹룸 멤버2 다짐에 이모션 등록
        Emotion emotion = Emotion.builder()
                .member(member2)
                .dajim(dajim3)
                .sticker(Sticker.valueOf("TOUCHED"))
                .build();

        Emotion savedEmotion = emotionRepository.save(emotion);
        dajim3.addEmotions(savedEmotion);

        //이모션 불러오기ㅜ
        List<Emotion> emotions = dajim3.getEmotions();
        List<String> stickersList = emotions.stream().map(emotion1 ->
                        emotion1.getSticker().toString())
                .collect(Collectors.toList());

        //해당 룸넘버들의 다짐 불러오기
        List<Dajim> dajims = dajimFeedRepository.findAllByOpen();

        //then
        for (Dajim c : dajims){
            System.out.println("다짐 넘버 리스트 : "+c.getNumber());
        }

        //멤버2
        assertThat(dajims.size()).isEqualTo(3); //다짐 4개
        assertThat(dajim3.getEmotions().get(0).getSticker().toString()).isEqualTo("TOUCHED");
        assertThat(stickersList.get(0)).isEqualTo("TOUCHED");
    }

}
