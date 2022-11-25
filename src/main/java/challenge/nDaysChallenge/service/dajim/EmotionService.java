package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Stickers;
import challenge.nDaysChallenge.dto.request.EmotionRequestDto;
import challenge.nDaysChallenge.repository.dajim.DajimFeedRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import challenge.nDaysChallenge.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EmotionService {

    private final EmotionRepository emotionRepository;

    public void uploadEmotion(EmotionRequestDto emotionRequestDto, UserDetailsImpl userDetailsImpl) { //스티커 등록/변경/삭제
        Member member = userDetailsImpl.getMember();

        Long dajimNumber = emotionRequestDto.getDajimNumber();
        Dajim dajim = emotionRepository.findByDajimNumber(dajimNumber);

        String sticker = emotionRequestDto.getSticker();
        Stickers stickers = Stickers.valueOf(sticker);

        Emotion emotion = Emotion.builder()
                        .member(member)
                        .dajim(dajim)
                        .stickers(stickers)
                        .build();

        emotionRepository.save(emotion); //createdDate 기준으로 등록 or 업데이트 (stickers에 null 대입 시 null로 업데이트)
    }

}
