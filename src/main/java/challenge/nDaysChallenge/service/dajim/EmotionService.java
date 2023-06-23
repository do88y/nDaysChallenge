package challenge.nDaysChallenge.service.dajim;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.dajim.Dajim;
import challenge.nDaysChallenge.domain.dajim.Emotion;
import challenge.nDaysChallenge.domain.dajim.Sticker;
import challenge.nDaysChallenge.dto.request.dajim.EmotionRequestDto;
import challenge.nDaysChallenge.dto.response.dajim.EmotionResponseDto;
import challenge.nDaysChallenge.exception.CustomError;
import challenge.nDaysChallenge.exception.CustomException;
import challenge.nDaysChallenge.repository.dajim.DajimRepository;
import challenge.nDaysChallenge.repository.dajim.EmotionRepository;
import challenge.nDaysChallenge.repository.member.MemberRepository;
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

    private final MemberRepository memberRepository;

    //스티커 등록
    public EmotionResponseDto uploadEmotion(EmotionRequestDto emotionRequestDto, String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomError.USER_NOT_FOUND));

        //이미 존재하는 이모션인지 확인 (한 다짐, 한 멤버 당 하나의 이모션)
        if (emotionRepository.findByDajim_NumberAndMember_Number(emotionRequestDto.getDajimNumber(), member.getNumber()).isPresent()){
            throw new CustomException(CustomError.EXISTING_EMOTION);
        }

        Sticker sticker = Sticker.valueOf(emotionRequestDto.getSticker());

        Dajim dajim = dajimRepository.findByNumber(emotionRequestDto.getDajimNumber())
            .orElseThrow(()->new CustomException(CustomError.DAJIM_NOT_FOUND));

        Emotion emotion = Emotion.builder()
                .member(member)
                .dajim(dajim)
                .sticker(sticker)
                .build();

        emotionRepository.save(emotion);

        dajim.addEmotions(emotion);

        return  EmotionResponseDto.of(emotionRequestDto, member);
    }

    public EmotionResponseDto updateEmotion(EmotionRequestDto emotionRequestDto, String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomError.USER_NOT_FOUND));

        Emotion emotion = emotionRepository.findByDajim_NumberAndMember_Number(emotionRequestDto.getDajimNumber(), member.getNumber())
                .orElseThrow(() -> new CustomException(CustomError.EMOTION_NOT_FOUND));

        emotion.update(Sticker.valueOf(emotionRequestDto.getSticker())); //수정 반영

        return  EmotionResponseDto.of(emotionRequestDto, member);
    }

    public EmotionResponseDto deleteEmotion(EmotionRequestDto emotionRequestDto, String id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomError.USER_NOT_FOUND));

        Emotion emotion = emotionRepository.findByDajim_NumberAndMember_Number(emotionRequestDto.getDajimNumber(), member.getNumber())
                .orElseThrow(() -> new CustomException(CustomError.EMOTION_NOT_FOUND));

        emotionRepository.delete(emotion);

        Dajim dajim = dajimRepository.findByNumber(emotionRequestDto.getDajimNumber())
                .orElseThrow(() -> new CustomException(CustomError.DAJIM_NOT_FOUND));

        dajim.deleteEmotions(emotion);

        return  EmotionResponseDto.of(emotionRequestDto, member);
    }

}
