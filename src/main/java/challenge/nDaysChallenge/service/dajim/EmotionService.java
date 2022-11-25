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

    private final DajimRepository dajimRepository;

    public void uploadEmotion(EmotionRequestDto emotionRequestDto, UserDetailsImpl userDetailsImpl) { //스티커 등록/변경/삭제
        Member member = userDetailsImpl.getMember();

        Dajim dajim = emotionRepository.findByDajimNumberForEmotion(emotionRequestDto.getDajimNumber());

        Stickers sticker = Stickers.valueOf(emotionRequestDto.getSticker());

        Emotion emotion = Emotion.builder()
                        .member(member)
                        .dajim(dajim)
                        .stickers(sticker)
                        .build();

        emotionRepository.save(emotion);
    }

    public Emotion updateEmotion(EmotionRequestDto requestDto, UserDetailsImpl userDetailsImpl){
        //수정할 이모션 객체 불러오기
        Emotion emotion = emotionRepository.findByEmotionNumber(requestDto.getDajimNumber(),
                                                                userDetailsImpl.getMember().getNumber());

        Emotion updatedEmotion;

        if (requestDto.getSticker()==null||requestDto.getSticker()==""){
            updatedEmotion = emotion.update(null);
        } else {
            updatedEmotion = emotion.update(Stickers.valueOf(requestDto.getSticker()));
        }

        return updatedEmotion;
    }

}
