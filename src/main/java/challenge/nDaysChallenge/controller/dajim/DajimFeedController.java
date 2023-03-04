package challenge.nDaysChallenge.controller.dajim;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.dto.request.dajim.DajimUpdateRequestDto;
import challenge.nDaysChallenge.dto.request.dajim.DajimUploadRequestDto;
import challenge.nDaysChallenge.dto.response.dajim.DajimResponseDto;
import challenge.nDaysChallenge.service.dajim.DajimFeedService;
import challenge.nDaysChallenge.service.dajim.DajimService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DajimFeedController {

    private final DajimFeedService dajimFeedService;

    //피드 전체 조회 (다짐 + 감정스티커)
    @GetMapping("/feed")
    public ResponseEntity<?> viewDajimOnFeed(
            @AuthenticationPrincipal @Nullable MemberAdapter memberAdapter,
            @PageableDefault(sort = "updatedDate", direction = Sort.Direction.DESC) Pageable pageable){
        Slice dajimPage;

        try {
            dajimPage = dajimFeedService.viewFeedLoggedIn(memberAdapter.getMember(), pageable);
        } catch (NullPointerException e) {
            dajimPage = dajimFeedService.viewFeedWithoutLogin(pageable);
        }

        return ResponseEntity.ok().body(dajimPage);

    }

}
