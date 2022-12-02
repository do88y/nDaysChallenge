package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.dto.request.EmotionRequestDto;
import challenge.nDaysChallenge.dto.response.EmotionResponseDto;
import challenge.nDaysChallenge.service.dajim.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class EmotionController { //감정 스티커 등록

    private final EmotionService emotionService;

    //감정 스티커 등록 (프론트 unselected 시)
    @PostMapping("/feed/emotion")
    public ResponseEntity<?> uploadEmotion(@RequestBody EmotionRequestDto emotionRequestDto,
                                           @AuthenticationPrincipal MemberAdapter memberAdapter){
        EmotionResponseDto emotionResponseDto = emotionService.uploadEmotion(emotionRequestDto, memberAdapter.getMember());

        return ResponseEntity.ok().body(emotionResponseDto);
    }

    //감정 스티커 변경/null (프론트 selected 시)
    @PutMapping("/feed/emotion")
    public ResponseEntity<?> updateEmotion(@RequestBody EmotionRequestDto emotionRequestDto,
                                         @AuthenticationPrincipal MemberAdapter memberAdapter){

        EmotionResponseDto emotionResponseDto = emotionService.updateEmotion(emotionRequestDto, memberAdapter.getMember());

        return ResponseEntity.ok().body(emotionResponseDto);
    }

}
