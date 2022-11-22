package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.dto.request.EmotionRequestDto;
import challenge.nDaysChallenge.dto.response.DajimResponseDto;
import challenge.nDaysChallenge.dto.response.EmotionResponseDto;
import challenge.nDaysChallenge.security.UserDetailsImpl;
import challenge.nDaysChallenge.service.dajim.DajimFeedService;
import challenge.nDaysChallenge.service.dajim.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DajimFeedController { //피드 내 다짐

    private final DajimFeedService dajimFeedService;
    private final EmotionService emotionsService;

    @GetMapping("/feed")
    //피드 전체 조회 (다짐 + 감정스티커 + 각스티커별 개수)
    public ResponseEntity<?> viewDajimOnFeed(@AuthenticationPrincipal UserDetailsImpl userDetailsImpl){

        //다짐, 스티커 List 대입
        List<DajimResponseDto> dajimsList = null;
        List<EmotionResponseDto> emotionsList = null;


        try {
//            dajimsList = dajimFeedService.viewDajimOnFeed(dajim,userDetailsImpl);
//            emotionsList = emotionsService.viewEmotionOnDajim(sticker, dajim, userDetailsImpl);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }

        //Map에 대입
        Map<String, Object> dajimsListMap = new HashMap<>();
        Map<String, Object> emotionsListMap = new HashMap<>();

        dajimsListMap.put("다짐 리스트",dajimsList);
        emotionsListMap.put("감정스티커 리스트",emotionsList);

        //최종 List
        List<Map> dajimFeed = new ArrayList<>();
        dajimFeed.add(dajimsListMap);
        dajimFeed.add(emotionsListMap);

        return ResponseEntity.ok().body(dajimFeed);
    }

}
