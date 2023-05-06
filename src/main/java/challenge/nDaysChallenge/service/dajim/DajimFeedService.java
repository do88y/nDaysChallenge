package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Open;
import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.room.Room;
import challenge.nDaysChallenge.dto.request.dajim.DajimUpdateRequestDto;
import challenge.nDaysChallenge.dto.request.dajim.DajimUploadRequestDto;
import challenge.nDaysChallenge.dto.response.dajim.DajimFeedResponseDto;
import challenge.nDaysChallenge.dto.response.dajim.DajimResponseDto;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.room.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DajimFeedService {

    private final DajimRepository dajimRepository;

    private final MemberRepository memberRepository;

    //피드 - 전체 다짐 조회 (미로그인)
    @PreAuthorize("isAnonymous()")
    @Transactional(readOnly = true)
    public Slice<DajimFeedResponseDto> viewFeedWithoutLogin(Pageable pageable) {
        Slice<Dajim> dajimPage = dajimRepository.findByOpen(Open.PUBLIC, pageable);

        List<DajimFeedResponseDto> dajimFeedList = toUnloggedInFeedDto(dajimPage);

        return new CustomSliceImpl<>(dajimFeedList, pageable, dajimPage.hasNext());
    }

    //피드 - 전체 다짐 조회 (로그인 시)
    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public Slice<DajimFeedResponseDto> viewFeedLoggedIn(String id, Pageable pageable) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 id의 사용자를 찾아오는 데 실패했습니다."));

        Slice<Dajim> dajimPage = dajimRepository.findByOpen(Open.PUBLIC, pageable);

        List<DajimFeedResponseDto> dajimFeedList = toLoggedInFeedDto(member, dajimPage);

        return new CustomSliceImpl<>(dajimFeedList,pageable, dajimPage.hasNext());
    }

    //미로그인 - 다짐 슬라이스 -> 피드 dto 리스트 변환
    private List<DajimFeedResponseDto> toUnloggedInFeedDto(Slice<Dajim> dajimPage){
        return dajimPage.getContent().stream()
                .map(dajim -> DajimFeedResponseDto.of(dajim, null))
                .collect(toList());
    }

    //로그인 - 다짐 슬라이스 -> 피드 dto 리스트 변환
    private List<DajimFeedResponseDto> toLoggedInFeedDto(Member member, Slice<Dajim> dajimPage) {
        return dajimPage.getContent().stream()
                .map(dajim -> DajimFeedResponseDto.of(dajim, member))
                .collect(toList());
    }

}

