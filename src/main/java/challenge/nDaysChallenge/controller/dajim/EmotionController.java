package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.dto.request.dajim.EmotionRequestDto;
import challenge.nDaysChallenge.dto.response.dajim.EmotionResponseDto;
import challenge.nDaysChallenge.service.dajim.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class EmotionController { //감정 스티커 등록

    private final EmotionService emotionService;

    //이모션 등록
    @PostMapping("/feed/emotion")
    public ResponseEntity<?> uploadEmotion(@RequestBody EmotionRequestDto emotionRequestDto, Principal principal){
        EmotionResponseDto emotionResponseDto = emotionService.uploadEmotion(emotionRequestDto, principal.getName());

        return ResponseEntity.ok().body(emotionResponseDto);
    }

    //이모션 수정
    @PatchMapping("/feed/emotion")
    public ResponseEntity<?> updateEmotion(@RequestBody EmotionRequestDto emotionRequestDto, Principal principal){
        EmotionResponseDto emotionResponseDto = emotionService.updateEmotion(emotionRequestDto, principal.getName());

        return ResponseEntity.ok().body(emotionResponseDto);
    }

    //이모션 삭제
    @DeleteMapping("/feed/emotion")
    public ResponseEntity<?> deleteEmotion(@RequestBody EmotionRequestDto emotionRequestDto, Principal principal){
        EmotionResponseDto emotionResponseDto = emotionService.deleteEmotion(emotionRequestDto, principal.getName());

        return ResponseEntity.ok().body(emotionResponseDto.getMemberNickname()+"님의 "+emotionResponseDto.getSticker()+" 스티커가 삭제되었습니다.");
    }

}
