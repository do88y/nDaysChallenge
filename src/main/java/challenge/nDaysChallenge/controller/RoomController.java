package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.Room.GroupRoomRequestDto;
import challenge.nDaysChallenge.dto.request.Room.RoomRequestDto;
import challenge.nDaysChallenge.dto.request.StampDto;
import challenge.nDaysChallenge.dto.response.Room.GroupRoomResponseDto;
import challenge.nDaysChallenge.dto.response.Room.RoomResponseDto;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomRepository roomRepository;
    private final RoomService roomService;

    //챌린지 리스트(메인)
    @GetMapping("/challenge/list")
    public ResponseEntity<?> list(@AuthenticationPrincipal MemberAdapter memberAdapter) {

        checkLogin(memberAdapter.getMember());
        List<RoomResponseDto> roomList = new ArrayList<>();
        
        List<SingleRoom> singleRooms = roomService.findSingleRooms(memberAdapter.getMember());
        for (SingleRoom singleRoom : singleRooms) {
            RoomResponseDto roomResponseDto = RoomResponseDto.builder()
                    .roomNumber(singleRoom.getNumber())
                    .name(singleRoom.getName())
                    .category(singleRoom.getCategory().name())
                    .reward(singleRoom.getReward())
                    .type(singleRoom.getType().name())
                    .status(singleRoom.getStatus().name())
                    .passCount(singleRoom.getPassCount())
                    .totalDays(singleRoom.getPeriod().getTotalDays())
                    .startDate(singleRoom.getPeriod().getStartDate())
                    .endDate(singleRoom.getPeriod().getEndDate())
                    .build();
            roomList.add(roomResponseDto);
        }

        List<GroupRoom> groupRooms = roomService.findGroupRooms(memberAdapter.getMember());
        for (GroupRoom groupRoom : groupRooms) {
            RoomResponseDto roomResponseDto = RoomResponseDto.builder()
                    .roomNumber(groupRoom.getNumber())
                    .name(groupRoom.getName())
                    .category(groupRoom.getCategory().name())
                    .reward(groupRoom.getReward())
                    .type(groupRoom.getType().name())
                    .status(groupRoom.getStatus().name())
                    .passCount(groupRoom.getPassCount())
                    .totalDays(groupRoom.getPeriod().getTotalDays())
                    .startDate(groupRoom.getPeriod().getStartDate())
                    .endDate(groupRoom.getPeriod().getEndDate())
                    .build();
            roomList.add(roomResponseDto);
        }
        
        return ResponseEntity.status(HttpStatus.OK).body(roomList);
    }

    //챌린지 상세
    @GetMapping("/challenge/{challengeId}")
    public ResponseEntity<?> detail(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                    @PathVariable("challengeId") Long roomNumber) {

        checkLogin(memberAdapter.getMember());
        Room room = roomRepository.findById(roomNumber).orElseThrow(
                () -> new NoSuchElementException("해당 챌린지가 없습니다"));

        RoomResponseDto roomDetail = RoomResponseDto.builder()
                .roomNumber(room.getNumber())
                .name(room.getName())
                .category(room.getCategory().name())
                .reward(room.getReward())
                .type(room.getType().name())
                .status(room.getStatus().name())
                .passCount(room.getPassCount())
                .totalDays(room.getPeriod().getTotalDays())
                .startDate(room.getPeriod().getStartDate())
                .endDate(room.getPeriod().getEndDate())
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(roomDetail);
    }

    //스탬프 찍기
    @PostMapping("/challenge/stamp")
    public ResponseEntity<?> stamp(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                   @RequestBody StampDto dto) {

        checkLogin(memberAdapter.getMember());

        Stamp stamp = roomService.updateStamp(memberAdapter.getMember(), dto.getRoomNumber(), dto);
        StampDto updatedStamp = new StampDto(dto.getRoomNumber(), stamp.getNumber(), stamp.getDay1(), stamp.getDay2(), stamp.getDay3(), stamp.getDay4(), stamp.getDay5(), stamp.getDay6(), stamp.getDay7(), stamp.getDay8(), stamp.getDay9(), stamp.getDay10(),
                                                            stamp.getDay11(), stamp.getDay12(), stamp.getDay13(), stamp.getDay14(), stamp.getDay15(), stamp.getDay16(), stamp.getDay17(), stamp.getDay18(), stamp.getDay19(), stamp.getDay20(),
                                                            stamp.getDay21(), stamp.getDay22(), stamp.getDay23(), stamp.getDay24(), stamp.getDay25(), stamp.getDay26(), stamp.getDay27(), stamp.getDay28(), stamp.getDay29(), stamp.getDay30());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedStamp);
    }

    //개인 챌린지 생성
    @PostMapping("/challenge/create")
    public ResponseEntity<?> singleRoom(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                        @RequestBody RoomRequestDto dto) {

        checkLogin(memberAdapter.getMember());

        Room room = roomService.singleRoom(memberAdapter.getMember(), dto.getName(), new Period(dto.getStartDate(), dto.getTotalDays()), Category.valueOf(dto.getCategory()), dto.getPassCount(), dto.getReward(), dto.getUsedPassCount(), dto.getSuccessCount());
        RoomResponseDto savedRoom = RoomResponseDto.builder()
                .roomNumber(room.getNumber())
                .name(room.getName())
                .category(room.getCategory().name())
                .reward(room.getReward())
                .type(room.getType().name())
                .status(room.getStatus().name())
                .passCount(room.getPassCount())
                .stamp(room.getStamp().getNumber())
                .totalDays(room.getPeriod().getTotalDays())
                .startDate(room.getPeriod().getStartDate())
                .endDate(room.getPeriod().getEndDate())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    //그룹 챌린지 생성
    @PostMapping("/challenge/createGroup")
    public ResponseEntity<?> groupRoom(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                       @RequestBody GroupRoomRequestDto dto) {

        checkLogin(memberAdapter.getMember());

        Room room = roomService.groupRoom(memberAdapter.getMember(), dto.getName(), new Period(dto.getStartDate(), dto.getTotalDays()), Category.valueOf(dto.getCategory()), dto.getPassCount(), dto.getReward(), dto.getUsedPassCount(), dto.getSuccessCount(), dto.getGroupMembers());
        GroupRoomResponseDto savedRoom = GroupRoomResponseDto.builder()
                .roomNumber(room.getNumber())
                .name(room.getName())
                .category(room.getCategory().name())
                .reward(room.getReward())
                .type(room.getType().name())
                .status(room.getStatus().name())
                .passCount(room.getPassCount())
                .stamp(room.getStamp().getNumber())
                .totalDays(room.getPeriod().getTotalDays())
                .startDate(room.getPeriod().getStartDate())
                .endDate(room.getPeriod().getEndDate())
                .groupMembers(dto.getGroupMembers())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
    }

    //챌린지 삭제&실패
    @DeleteMapping("/challenge/{challengeId}")
    public ResponseEntity<?> delete(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                    @PathVariable("challengeId") Long roomNumber) {

        checkLogin(memberAdapter.getMember());

        roomService.deleteRoom(memberAdapter.getMember(), roomNumber);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    //완료 챌린지 상태 변경
    @PostMapping("/challenge/{challengeId}/success")
    public ResponseEntity<?> end(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                 @PathVariable("challengeId") Long roomNumber) {

        roomService.changeStatus(roomNumber);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    //마이페이지 - 완료 챌린지 조회
    @GetMapping("/user/finishedChallenges")
    public ResponseEntity<?> finishedRooms(@AuthenticationPrincipal MemberAdapter memberAdapter) {

        checkLogin(memberAdapter.getMember());
        List<RoomResponseDto> finishedRooms = new ArrayList<>();

        List<SingleRoom> findSingleRooms = roomService.findFinishedSingleRooms(memberAdapter.getMember());
        for (Room room : findSingleRooms) {
            RoomResponseDto finishedRoom = RoomResponseDto.builder()
                    .roomNumber(room.getNumber())
                    .name(room.getName())
                    .category(room.getCategory().name())
                    .reward(room.getReward())
                    .type(room.getType().name())
                    .status(room.getStatus().name())
                    .passCount(room.getPassCount())
                    .totalDays(room.getPeriod().getTotalDays())
                    .startDate(room.getPeriod().getStartDate())
                    .endDate(room.getPeriod().getEndDate())
                    .build();
            finishedRooms.add(finishedRoom);
        }

        List<GroupRoom> findGroupRooms = roomService.findFinishedGroupRooms(memberAdapter.getMember());
        for (Room room : findGroupRooms) {
            RoomResponseDto finishedRoom = RoomResponseDto.builder()
                    .roomNumber(room.getNumber())
                    .name(room.getName())
                    .category(room.getCategory().name())
                    .reward(room.getReward())
                    .type(room.getType().name())
                    .status(room.getStatus().name())
                    .passCount(room.getPassCount())
                    .totalDays(room.getPeriod().getTotalDays())
                    .startDate(room.getPeriod().getStartDate())
                    .endDate(room.getPeriod().getEndDate())
                    .build();
            finishedRooms.add(finishedRoom);
        }

        return ResponseEntity.status(HttpStatus.OK).body(finishedRooms);
    }

    //로그인 검증
    private void checkLogin(Member member) {
        if (member == null) {
            throw new RuntimeException("로그인한 멤버만 사용할 수 있습니다.");
        }
    }

}
