package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.dto.response.DajimFeedResponseDto;
import challenge.nDaysChallenge.service.dajim.DajimFeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class DajimFeedController { //피드 내 다짐

    private final DajimFeedService dajimFeedService;

    @GetMapping("/feed")
    //피드 전체 조회 (다짐 + 감정스티커 리스트)
    public ResponseEntity<?> viewDajimOnFeed(@AuthenticationPrincipal User user){

        List<Dajim> dajims = dajimFeedService.viewDajimOnFeed(user);

        List<DajimFeedResponseDto> dajimFeedList = dajims.stream().map(dajim ->
                new DajimFeedResponseDto(
                        dajim.getNumber(),
                        dajim.getMember().getNickname(),
                        dajim.getContent(),
                        dajim.getEmotions().stream().map(emotion ->
                                        emotion.getStickers().toString())
                                .collect(Collectors.toList()),
                        dajim.getUpdatedDate()
                )).collect(Collectors.toList());

        return ResponseEntity.ok().body(dajimFeedList);
    }

}
