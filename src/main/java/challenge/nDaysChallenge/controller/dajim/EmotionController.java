package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.domain.Member;
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

    @PostMapping("/feed/emotion") //감정 스티커 등록
    public ResponseEntity<?> uploadEmotion(@RequestBody EmotionRequestDto emotionRequestDto,
                                           @AuthenticationPrincipal User user){
        Emotion emotion = emotionService.uploadEmotion(emotionRequestDto, user);

        EmotionResponseDto savedEmotion = new EmotionResponseDto(emotionRequestDto.getDajimNumber(),
                                                                    emotion.getMember().getNickname(),
                                                                    emotion.getStickers().toString());

        return ResponseEntity.ok().body(savedEmotion);
    }

    @PutMapping("/feed/emotion")
    public ResponseEntity<?> updateEmotion(@RequestBody EmotionRequestDto emotionRequestDto,
                                         @AuthenticationPrincipal User user){

        Emotion emotion = emotionService.updateEmotion(emotionRequestDto, user);

        EmotionResponseDto updatedEmotion = EmotionResponseDto.builder()
                .dajimNumber(emotion.getDajim().getNumber())
                .memberNickname(emotion.getMember().getNickname())
                .stickers(emotion.getStickers().toString())
                .build();

        return ResponseEntity.ok().body(updatedEmotion);
    }

}
