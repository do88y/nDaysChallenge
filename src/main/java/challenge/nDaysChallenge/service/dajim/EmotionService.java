package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Sticker;
import challenge.nDaysChallenge.dto.request.dajim.EmotionRequestDto;
import challenge.nDaysChallenge.dto.response.dajim.EmotionResponseDto;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EmotionService {

    private final EmotionRepository emotionRepository;

    private final DajimRepository dajimRepository;

    //스티커 등록/변경/삭제
    public EmotionResponseDto uploadEmotion(EmotionRequestDto emotionRequestDto, Member member) {
        Sticker sticker = Sticker.valueOf(emotionRequestDto.getSticker());

        Dajim dajim = dajimRepository.findByNumber(emotionRequestDto.getDajimNumber())
            .orElseThrow(()->new RuntimeException("감정 스티커를 등록할 다짐을 찾을 수 없습니다."));

        Emotion emotion = Emotion.builder()
                .member(member)
                .dajim(dajim)
                .sticker(sticker)
                .build();

        emotionRepository.save(emotion);

        dajim.addEmotions(emotion);

        return  EmotionResponseDto.of(emotionRequestDto, member);
    }

    public EmotionResponseDto updateEmotion(EmotionRequestDto emotionRequestDto, Member member) {
        Emotion emotion = emotionRepository.findByDajimAndMember(emotionRequestDto.getDajimNumber(), member.getNumber())
                .orElseThrow(()->new RuntimeException("수정할 이모션을 찾을 수 없습니다."));

        emotion.update(Sticker.valueOf(emotionRequestDto.getSticker())); //수정 반영

        return  EmotionResponseDto.of(emotionRequestDto, member);
    }

    public EmotionResponseDto deleteEmotion(EmotionRequestDto emotionRequestDto, Member member) {

        Emotion emotion = emotionRepository.findByDajimAndMember(emotionRequestDto.getDajimNumber(), member.getNumber())
                .orElseThrow(()->new RuntimeException("수정할 이모션을 찾을 수 없습니다."));

        emotionRepository.delete(emotion);

        Dajim dajim = dajimRepository.findByNumber(emotionRequestDto.getDajimNumber())
                .orElseThrow(()->new RuntimeException("이모션이 소속된 다짐을 찾을 수 없습니다."));

        dajim.deleteEmotions(emotion);

        return  EmotionResponseDto.of(emotionRequestDto, member);
    }

}
