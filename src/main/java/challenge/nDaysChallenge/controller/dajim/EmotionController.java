package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.dto.request.EmotionRequestDto;
import challenge.nDaysChallenge.dto.response.DajimResponseDto;
import challenge.nDaysChallenge.dto.response.EmotionResponseDto;
import challenge.nDaysChallenge.security.UserDetailsImpl;
import challenge.nDaysChallenge.service.dajim.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmotionController { //감정 스티커 등록

    private final EmotionService emotionService;

    @GetMapping("/feed/emotion") //감정 스티커 등록/변경/삭제
    public ResponseEntity<?> uploadEmotion(@RequestBody EmotionRequestDto emotionRequestDto,
                                           @AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        emotionService.uploadEmotion(emotionRequestDto,userDetailsImpl);
        EmotionResponseDto savedEmotion = new EmotionResponseDto(emotionRequestDto.getDajimNumber(),
                                                                    userDetailsImpl.getMember().getNumber(),
                                                                    emotionRequestDto.getSticker());

        return ResponseEntity.ok().body(savedEmotion);
    }

}
