package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.dto.request.dajim.EmotionRequestDto;
import challenge.nDaysChallenge.dto.response.dajim.EmotionResponseDto;
import challenge.nDaysChallenge.service.dajim.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EmotionController { //감정 스티커 등록

    private final EmotionService emotionService;

    //이모션 등록/수정/삭제
    @PostMapping("/feed/emotion")
    public ResponseEntity<?> manageEmotion(@RequestBody EmotionRequestDto emotionRequestDto,
                                           @AuthenticationPrincipal MemberAdapter memberAdapter){
        EmotionResponseDto emotionResponseDto = emotionService.manageEmotion(emotionRequestDto, memberAdapter.getMember());

        return ResponseEntity.ok().body(emotionResponseDto);
    }

//    //감정 스티커 new 등록 (프론트 unselected된 상태에서)
//    @PostMapping("/feed/emotion")
//    public ResponseEntity<?> uploadEmotion(@RequestBody EmotionRequestDto emotionRequestDto,
//                                           @AuthenticationPrincipal MemberAdapter memberAdapter){
//        EmotionResponseDto emotionResponseDto = emotionService.uploadEmotion(emotionRequestDto, memberAdapter.getMember());
//
//        return ResponseEntity.ok().body(emotionResponseDto);
//    }
//
//    //감정 스티커 변경 or null 등록 (프론트 selected된 상태에서 다른 스티커 select 시 or 두번 똑같은 스티커 클릭 시)
//    @PatchMapping("/feed/emotion")
//    public ResponseEntity<?> updateEmotion(@RequestBody EmotionRequestDto emotionRequestDto,
//                                         @AuthenticationPrincipal MemberAdapter memberAdapter){
//
//        EmotionResponseDto emotionResponseDto = emotionService.updateEmotion(emotionRequestDto, memberAdapter.getMember());
//
//        return ResponseEntity.ok().body(emotionResponseDto);
//    }

}
