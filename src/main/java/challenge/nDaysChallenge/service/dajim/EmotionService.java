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
    public EmotionResponseDto manageEmotion(EmotionRequestDto emotionRequestDto, Member member) {
        Sticker sticker = Sticker.valueOf(emotionRequestDto.getSticker());

        Dajim dajim = dajimRepository.findByNumber(emotionRequestDto.getDajimNumber())
            .orElseThrow(()->new RuntimeException("감정 스티커를 등록할 다짐을 찾을 수 없습니다."));

        Optional<Emotion> emotion = emotionRepository.findByDajimAndMember(emotionRequestDto.getDajimNumber(), member.getNumber());

        EmotionResponseDto emotionResponseDto = EmotionResponseDto.of(emotionRequestDto, member);

        //이모션 객체 등록 (레포지토리에서 객체 못 찾았을 때)
        if (!emotion.isPresent()){
            uploadEmotion(member,dajim,sticker);
            return emotionResponseDto;
        }

        //스티커 수정 (다른 스티커 클릭 시)
        Emotion emotionForUpdate = emotion.get();
        Sticker stickerBeforeUpdate = emotionForUpdate.getSticker();
        LocalDateTime beforeUpdate = emotionForUpdate.getUpdatedDate(); //수정 전 마지막 업데이트 시간 저장해두기

        Emotion updatedEmotion = emotionForUpdate.update(Sticker.valueOf(emotionRequestDto.getSticker())); //수정 반영

        //이모션 객체 삭제 (동일 스티커 클릭해서 업데이트 안 됐을 때)
        LocalDateTime afterUpdate = updatedEmotion.getUpdatedDate();
        if (beforeUpdate.isEqual(afterUpdate) && //업데이트 X & 똑같은 스티커 클릭 시
                stickerBeforeUpdate.toString().equals(emotionRequestDto.getSticker())){

            emotionRepository.delete(updatedEmotion);
            dajim.deleteEmotions(updatedEmotion);

            emotionResponseDto.updateNull();
        }

        return emotionResponseDto;
    }

    private void uploadEmotion(Member member, Dajim dajim, Sticker sticker){ //이모션 등록
        Emotion emotion = Emotion.builder()
                .member(member)
                .dajim(dajim)
                .sticker(sticker)
                .build();

        emotionRepository.save(emotion);

        dajim.addEmotions(emotion); //다짐 엔티티 이모션리스트에 추가
    }

    //이모션 등록
//    public EmotionResponseDto uploadEmotion(EmotionRequestDto emotionRequestDto, Member member) { //스티커 등록/변경/삭제
//        Dajim dajim = dajimRepository.findByDajimNumber(emotionRequestDto.getDajimNumber())
//                .orElseThrow(()->new RuntimeException("감정 스티커를 등록할 다짐을 찾을 수 없습니다."));
//
//        Stickers sticker = Stickers.valueOf(emotionRequestDto.getSticker());
//
//        Emotion emotion = Emotion.builder()
//                        .member(member)
//                        .dajim(dajim)
//                        .stickers(sticker)
//                        .build();
//
//        emotionRepository.save(emotion);
//
//        dajim.addEmotions(emotion); //다짐 엔티티 이모션리스트에 추가
//
//        EmotionResponseDto emotionResponseDto = new EmotionResponseDto(
//                emotion.getDajim().getNumber(),
//                emotion.getMember().getNickname(),
//                emotion.getStickers().toString());
//
//        return emotionResponseDto;
//    }
//
//    //이모션 변경 및 삭제
//    public EmotionResponseDto updateEmotion(EmotionRequestDto requestDto, Member member){
//        //수정할 이모션 객체 불러오기
//        Emotion emotion = emotionRepository.findByEmotionNumber(requestDto.getDajimNumber(), member.getNumber())
//                .orElseThrow(()->new RuntimeException("감정 스티커를 불러오는 데 실패했습니다."));
//
//        //다짐 엔티티 내 emotionlist에 변경사항 반영 위해 다짐 불러오기
//        Long dajimNumber = requestDto.getDajimNumber();
//        Dajim dajim = dajimRepository.findByDajimNumber(dajimNumber)
//                .orElseThrow(()->new RuntimeException("이모션이 소속된 다짐을 찾을 수 없습니다."));
//
//        Emotion updatedEmotion;
//
//        //뷰에서 null or 다른 스티커 등록 시
//        if (requestDto.getSticker()==null||requestDto.getSticker().equals("")){
//            updatedEmotion = emotion.update(null);
//        } else {
//            updatedEmotion = emotion.update(Stickers.valueOf(requestDto.getSticker()));
//        }
//
//        EmotionResponseDto emotionResponseDto = EmotionResponseDto.builder()
//                .dajimNumber(updatedEmotion.getDajim().getNumber())
//                .memberNickname(updatedEmotion.getMember().getNickname())
//                .stickers(updatedEmotion.getStickers().toString())
//                .build();
//
//        return emotionResponseDto;
//    }

}
