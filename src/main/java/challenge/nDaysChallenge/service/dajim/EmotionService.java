package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Stickers;
import challenge.nDaysChallenge.dto.request.DajimRequestDto;
import challenge.nDaysChallenge.dto.request.EmotionRequestDto;
import challenge.nDaysChallenge.repository.dajim.DajimFeedRepository;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
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

    //이모션 등록
    public Emotion uploadEmotion(EmotionRequestDto emotionRequestDto, UserDetailsImpl userDetailsImpl) { //스티커 등록/변경/삭제
        Member member = userDetailsImpl.getMember();

        Dajim dajim = emotionRepository.findByDajimNumberForEmotion(emotionRequestDto.getDajimNumber())
                .orElseThrow(()->new RuntimeException("감정 스티커를 등록할 다짐을 찾을 수 없습니다."));

        Stickers sticker = Stickers.valueOf(emotionRequestDto.getSticker());

        Emotion emotion = Emotion.builder()
                        .member(member)
                        .dajim(dajim)
                        .stickers(sticker)
                        .build();

        Emotion savedEmotion = emotionRepository.save(emotion);

        return savedEmotion;
    }

    //이모션 변경 및 삭제
    public Emotion updateEmotion(EmotionRequestDto requestDto, UserDetailsImpl userDetailsImpl){
        //수정할 이모션 객체 불러오기
        Emotion emotion = emotionRepository.findByEmotionNumber(requestDto.getDajimNumber(),
                                                                userDetailsImpl.getMember().getNumber())
                .orElseThrow(()->new RuntimeException("감정 스티커를 불러오는 데 실패했습니다."));

        Emotion updatedEmotion;

        if (requestDto.getSticker()==null||requestDto.getSticker().equals("")){
            updatedEmotion = emotion.update(null);
        } else {
            updatedEmotion = emotion.update(Stickers.valueOf(requestDto.getSticker()));
        }

        return updatedEmotion;
    }

}
