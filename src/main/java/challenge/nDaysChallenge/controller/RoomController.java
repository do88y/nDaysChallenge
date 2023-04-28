package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.Room.GroupRoomRequestDto;
import challenge.nDaysChallenge.dto.request.Room.RoomRequestDto;
import challenge.nDaysChallenge.dto.request.StampDto;
import challenge.nDaysChallenge.dto.response.room.GroupRoomResponseDto;
import challenge.nDaysChallenge.dto.response.room.RoomResponseDto;
import challenge.nDaysChallenge.repository.StampRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomRepository roomRepository;
    private final RoomService roomService;

    //챌린지 리스트(메인)
    @GetMapping("/challenge/list")
    public ResponseEntity<?> list(@AuthenticationPrincipal MemberAdapter memberAdapter) {

        checkLogin(memberAdapter.getMember());

        List<SingleRoom> singleRooms = roomService.findSingleRooms(memberAdapter.getMember());
        List<GroupRoom> groupRooms = roomService.findGroupRooms(memberAdapter.getMember());

        return getResponseEntity(singleRooms, groupRooms);
    }

    //개인 챌린지 상세
    @GetMapping("/challenge/{challengeId}")
    public ResponseEntity<?> detail(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                    @PathVariable("challengeId") Long roomNumber) {

        checkLogin(memberAdapter.getMember());

        Room room = roomRepository.findByNumber(roomNumber).orElseThrow(
                () -> new NoSuchElementException("해당 챌린지가 없습니다"));

        return getRoomResponseDto(room, room.getStamp());
    }

    //개인 챌린지 생성
    @PostMapping("/challenge")
    public ResponseEntity<?> singleRoom(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                        @RequestBody RoomRequestDto dto) {

        checkLogin(memberAdapter.getMember());

        RoomResponseDto room = roomService.singleRoom(memberAdapter.getMember(), dto.getName(), new Period(dto.getStartDate(), dto.getTotalDays()), Category.valueOf(dto.getCategory()), dto.getPassCount(), dto.getReward());

        URI location = URI.create("/challenge/" + room.getRoomNumber());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(room);
    }

    //그룹 챌린지 생성
    @PostMapping("/challenge/group")
    public ResponseEntity<?> groupRoom(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                       @RequestBody GroupRoomRequestDto dto) {

        checkLogin(memberAdapter.getMember());

        GroupRoom room = roomService.groupRoom(memberAdapter.getMember(), dto.getName(), new Period(dto.getStartDate(), dto.getTotalDays()), Category.valueOf(dto.getCategory()), dto.getPassCount(), dto.getReward(), dto.getGroupMembers());
        GroupRoomResponseDto savedRoom = getGroupRoomResponseDto(dto, room);

        URI location = URI.create("/challenge/" + room.getNumber());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(savedRoom);
    }

    //스탬프 찍기
    @PostMapping("/challenge/stamp")
    public ResponseEntity<?> stamp(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                   @RequestBody StampDto dto) {

        checkLogin(memberAdapter.getMember());

        StampDto updatedStamp = roomService.updateStamp(memberAdapter.getMember(), dto.getRoomNumber(), dto);

        URI location = URI.create("/challenge/" + updatedStamp.getStampNumber());
        return ResponseEntity.status(HttpStatus.OK).location(location).body(updatedStamp);
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
    @PostMapping("/challenge/{challengeId}")
    public ResponseEntity<?> end(@PathVariable("challengeId") Long roomNumber) {

        Room findRoom = roomRepository.findByNumber(roomNumber).orElseThrow(
                () -> new NoSuchElementException("해당 챌린지가 존재하지 않습니다."));
        roomService.changeStatus(findRoom.getNumber());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    //마이페이지 - 완료 챌린지 조회
    @GetMapping("/user/challenges")
    public ResponseEntity<?> finishedRooms(@AuthenticationPrincipal MemberAdapter memberAdapter) {

        checkLogin(memberAdapter.getMember());

        List<RoomResponseDto> roomList = roomService.findFinishedSingleRooms(memberAdapter.getMember());
        return ResponseEntity.status(HttpStatus.OK).body(roomList);
    }


    //==공통 메서드==//
    //개인, 그룹 concat 후 생성 메서드 호출
    private ResponseEntity<?> getResponseEntity(List<SingleRoom> singleRooms, List<GroupRoom> groupRooms) {

        List<Room> rooms = Stream.concat(singleRooms.stream(), groupRooms.stream()).collect(Collectors.toList());

        List<RoomResponseDto> roomList = new ArrayList<>();
        for (Room room : rooms) {
            RoomResponseDto roomResponseDto = createRoomDto(room);
            roomList.add(roomResponseDto);
        }

        return ResponseEntity.status(HttpStatus.OK).body(roomList);
    }

    //room 응답 dto 생성
    private RoomResponseDto createRoomDto(Room room) {
        RoomResponseDto roomResponseDto = RoomResponseDto.builder()
                .roomNumber(room.getNumber())
                .type(room.getType().name())
                .name(room.getName())
                .category(room.getCategory().name())
                .totalDays(room.getPeriod().getTotalDays())
                .startDate(room.getPeriod().getStartDate())
                .endDate(room.getPeriod().getEndDate())
                .passCount(room.getPassCount())
                .reward(room.getReward())
                .status(room.getStatus().name())
                .build();
        return roomResponseDto;
    }

    //그룹챌린지 생성 응답 데이터
    private static GroupRoomResponseDto getGroupRoomResponseDto(GroupRoomRequestDto dto, GroupRoom room) {
        GroupRoomResponseDto savedRoom = GroupRoomResponseDto.builder()
                .roomNumber(room.getNumber())
                .type(room.getType().name())
                .name(room.getName())
                .category(room.getCategory().name())
                .totalDays(room.getPeriod().getTotalDays())
                .startDate(room.getPeriod().getStartDate())
                .endDate(room.getPeriod().getEndDate())
                .passCount(room.getPassCount())
                .reward(room.getReward())
                .status(room.getStatus().name())
                .groupMembers(dto.getGroupMembers())
                .build();
        return savedRoom;
    }

    //챌린지 상세조회 응답 데이터
    private static ResponseEntity<RoomResponseDto> getRoomResponseDto(Room room, Stamp findstamp) {
        RoomResponseDto roomDetail = RoomResponseDto.builder()
                .roomNumber(room.getNumber())
                .type(room.getType().name())
                .name(room.getName())
                .category(room.getCategory().name())
                .totalDays(room.getPeriod().getTotalDays())
                .startDate(room.getPeriod().getStartDate())
                .endDate(room.getPeriod().getEndDate())
                .passCount(room.getPassCount())
                .reward(room.getReward())
                .status(room.getStatus().name())
                .stamp(findstamp.getNumber())
                .day(findstamp.getDay())
                .successCount(findstamp.getSuccessCount())
                .usedPassCount(findstamp.getUsedPassCount())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(roomDetail);
    }

    //로그인 검증
    private void checkLogin(Member member) {
        if (member == null) {
            throw new RuntimeException("로그인한 멤버만 사용할 수 있습니다.");
        }
    }

}
