package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.domain.room.RoomType;
import challenge.nDaysChallenge.dto.request.RoomRequestDTO;
import challenge.nDaysChallenge.dto.response.MemberResponseDto;
import challenge.nDaysChallenge.dto.response.RoomResponseDto;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import challenge.nDaysChallenge.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    //챌린지 리스트(메인)
    @GetMapping("/challenge/list")
    public ResponseEntity<?> roomList(@AuthenticationPrincipal MemberAdapter memberAdapter) {

        List<RoomResponseDto> roomList = new ArrayList<>();

        return ResponseEntity.status(HttpStatus.OK).body(roomList);
    }




    //챌린지 생성
    @PostMapping("/challenge/create")
    public ResponseEntity<?> createRoom(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                        @RequestBody RoomRequestDTO roomRequestDTO) {

        Room room = roomService.createRoom(memberAdapter.getMember(), roomRequestDTO);

        if (roomRequestDTO.getType().equals(RoomType.SINGLE.name())) {
            RoomResponseDto savedRoom = RoomResponseDto.builder()
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
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
        }
        if (roomRequestDTO.getType().equals(RoomType.GROUP.name())) {
            RoomResponseDto savedRoom = RoomResponseDto.builder()
                    .name(room.getName())
                    .category(room.getCategory().name())
                    .reward(room.getReward())
                    .type(room.getType().name())
                    .status(room.getStatus().name())
                    .passCount(room.getPassCount())
                    .totalDays(room.getPeriod().getTotalDays())
                    .startDate(room.getPeriod().getStartDate())
                    .endDate(room.getPeriod().getEndDate())
                    .groupMembers(roomRequestDTO.getGroupMembers())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(savedRoom);
        }
        return null;
    }

    //챌린지 삭제&실패
    @DeleteMapping("/challenge/{challengeId}")
    public ResponseEntity<?> deleteRoom(@AuthenticationPrincipal MemberAdapter memberAdapter,
                                        @PathVariable("challengeId") Long roomNumber) {

        roomService.deleteRoom(memberAdapter.getMember(), roomNumber);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    //마이페이지 - 완료챌린지 전체 조회
    @GetMapping("/user/finishedChallenges")
    public ResponseEntity<?> findFinishedRooms(@AuthenticationPrincipal MemberAdapter memberAdapter) {

        List<Room> findRooms = roomService.findFinishedRooms(memberAdapter.getMember());
        List<RoomResponseDto> finishedRooms = new ArrayList<>();
        for (Room room : findRooms) {
            RoomResponseDto finishedRoom = RoomResponseDto.builder()
                    .number(room.getNumber())
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

}
