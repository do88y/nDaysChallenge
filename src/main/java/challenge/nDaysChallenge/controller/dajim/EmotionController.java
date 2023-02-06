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

@RestController
@RequiredArgsConstructor
public class EmotionController { //감정 스티커 등록

    private final EmotionService emotionService;

    //이모션 등록/수정/삭제
    @PostMapping("/feed/emotion")
    public ResponseEntity<?> uploadEmotion(@RequestBody EmotionRequestDto emotionRequestDto,
                                           @AuthenticationPrincipal MemberAdapter memberAdapter){
        checkLogin(memberAdapter.getMember());

        EmotionResponseDto emotionResponseDto = emotionService.uploadEmotion(emotionRequestDto, memberAdapter.getMember());

        return ResponseEntity.ok().body(emotionResponseDto);
    }

    @PatchMapping("/feed/emotion")
    public ResponseEntity<?> updateEmotion(@RequestBody EmotionRequestDto emotionRequestDto,
                                           @AuthenticationPrincipal MemberAdapter memberAdapter){
        checkLogin(memberAdapter.getMember());

        EmotionResponseDto emotionResponseDto = emotionService.updateEmotion(emotionRequestDto, memberAdapter.getMember());

        return ResponseEntity.ok().body(emotionResponseDto);
    }

    @DeleteMapping("/feed/emotion")
    public ResponseEntity<?> deleteEmotion(@RequestBody EmotionRequestDto emotionRequestDto,
                                           @AuthenticationPrincipal MemberAdapter memberAdapter){
        checkLogin(memberAdapter.getMember());

        EmotionResponseDto emotionResponseDto = emotionService.deleteEmotion(emotionRequestDto, memberAdapter.getMember());

        return ResponseEntity.ok().body(emotionResponseDto.getMemberNickname()+"님의 "+emotionResponseDto.getSticker()+" 스티커가 삭제되었습니다.");
    }

    private void checkLogin(Member member) {
        if (member == null) {
            throw new RuntimeException("로그인한 멤버만 사용할 수 있습니다.");
        }
    }

}
