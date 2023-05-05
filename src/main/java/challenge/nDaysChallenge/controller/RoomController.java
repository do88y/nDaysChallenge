package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.Stamp;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.domain.room.*;
import challenge.nDaysChallenge.dto.request.Room.GroupRoomRequestDto;
import challenge.nDaysChallenge.dto.request.Room.RoomRequestDto;
import challenge.nDaysChallenge.dto.request.StampDto;
import challenge.nDaysChallenge.dto.response.room.GroupRoomResponseDto;
import challenge.nDaysChallenge.dto.response.room.RoomResponseDto;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;
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
    public ResponseEntity<?> list(Principal principal) {
        List<RoomResponseDto> rooms = roomService.findRooms(principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(rooms);
    }

    //개인 챌린지 상세
    @GetMapping("/challenge/{challengeId}")
    public ResponseEntity<?> detail(Principal principal, @PathVariable("challengeId") Long roomNumber) {
        Room room = roomRepository.findByNumber(roomNumber).orElseThrow(
                () -> new NoSuchElementException("해당 챌린지가 없습니다"));

        return getRoomResponseDto(room, room.getStamp());
    }

    //개인 챌린지 생성
    @PostMapping("/challenge")
    public ResponseEntity<?> singleRoom(Principal principal, @RequestBody RoomRequestDto dto) {
        RoomResponseDto room = roomService.singleRoom(principal.getName(), dto.getName(), new Period(dto.getStartDate(), dto.getTotalDays()), Category.valueOf(dto.getCategory()), dto.getPassCount(), dto.getReward());

        URI location = URI.create("/challenge/" + room.getRoomNumber());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(room);
    }

    //그룹 챌린지 생성
    @PostMapping("/challenge/group")
    public ResponseEntity<?> groupRoom(Principal principal, @RequestBody GroupRoomRequestDto dto) {
        GroupRoomResponseDto room = roomService.groupRoom(principal.getName(), dto.getName(), new Period(dto.getStartDate(), dto.getTotalDays()), Category.valueOf(dto.getCategory()), dto.getPassCount(), dto.getReward(), dto.getGroupMembers());

        URI location = URI.create("/challenge/" + room.getRoomNumber());
        return ResponseEntity.status(HttpStatus.CREATED).location(location).body(room);
    }

    //스탬프 찍기
    @PostMapping("/challenge/stamp")
    public ResponseEntity<?> stamp(Principal principal, @RequestBody StampDto dto) {
        StampDto updatedStamp = roomService.updateStamp(principal.getName(), dto.getRoomNumber(), dto);

        URI location = URI.create("/challenge/" + updatedStamp.getStampNumber());
        return ResponseEntity.status(HttpStatus.OK).location(location).body(updatedStamp);
    }

    //챌린지 삭제&실패
    @DeleteMapping("/challenge/{challengeId}")
    public ResponseEntity<?> delete(Principal principal, @PathVariable("challengeId") Long roomNumber) {
        roomService.deleteRoom(principal.getName(), roomNumber);
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
    public ResponseEntity<?> finishedRooms(Principal principal) {

        List<RoomResponseDto> roomList = roomService.findFinishedRooms(principal.getName());
        return ResponseEntity.status(HttpStatus.OK).body(roomList);
    }


    //==공통 메서드==//
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

}
