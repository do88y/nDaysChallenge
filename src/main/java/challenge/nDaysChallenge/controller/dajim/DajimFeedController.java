package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.dto.request.DajimCommentRequestDto;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import challenge.nDaysChallenge.dto.request.LikesRequestDto;
import challenge.nDaysChallenge.dto.response.DajimCommentResponseDto;
import challenge.nDaysChallenge.dto.response.DajimResponseDto;
import challenge.nDaysChallenge.dto.response.LikesResponseDto;
import challenge.nDaysChallenge.security.UserDetailsImpl;
import challenge.nDaysChallenge.service.dajim.DajimCommentService;
import challenge.nDaysChallenge.service.dajim.DajimFeedService;
import challenge.nDaysChallenge.service.dajim.LikesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class DajimFeedController { //피드 내 다짐

    private final DajimFeedService dajimFeedService;
    private final LikesService likesService;
    private final DajimCommentService dajimCommentService;

    @GetMapping("/feed")
    //다짐 피드 전체 (다짐 + 좋아요 + 댓글)
    public ResponseEntity<?> viewDajimOnFeed(DajimRequestDto dajimRequestDto, LikesRequestDto likesRequestDto, DajimCommentRequestDto dajimCommentRequestDto,
                                             @AuthenticationPrincipal UserDetailsImpl userDetailsImpl){
        //다짐, 좋아요, 댓글 List 대입
        List<DajimResponseDto> dajimsList;
        List<LikesResponseDto> likesList;
        List<DajimCommentResponseDto> dajimCommentsList;

        try {
            dajimsList = dajimFeedService.viewDajimOnFeed(roomNumber,userDetailsImpl);
            likesList = likesService.viewLikesOnDajim(dajimNumber, userDetailsImpl);
            dajimCommentsList = dajimCommentService.viewDajimCommentsOnDajim(dajimNumber, userDetailsImpl);
        } catch (Exception e){
            return ResponseEntity.notFound().build();
        }

        //Map에 대입
        Map<String, Object> dajimsListMap = new HashMap<>();
        Map<String, Object> likesListMap = new HashMap<>();
        Map<String, Object> dajimCommentsListMap = new HashMap<>();

        dajimsListMap.put("다짐 리스트",dajimsList);
        likesListMap.put("좋아요 리스트",likesList);
        dajimCommentsListMap.put("댓글 리스트",dajimCommentsList);

        //최종 List
        List<Map> dajimFeed = new ArrayList<>();
        dajimFeed.add(dajimsListMap);
        dajimFeed.add(likesListMap);
        dajimFeed.add(dajimCommentsListMap);

        return ResponseEntity.ok().body(dajimFeed);
    }

}
