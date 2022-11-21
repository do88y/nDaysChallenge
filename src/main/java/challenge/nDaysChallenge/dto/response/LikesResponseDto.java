package challenge.nDaysChallenge.dto.response;

import challenge.nDaysChallenge.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LikesResponseDto {

    boolean likes;

    Long dajimNumber; //좋아요 등록한 다짐 넘버

    Long memberNumber;//좋아요 등록한 멤버 넘버

    Long likesCount; //좋아요 개수

}
