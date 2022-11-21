package challenge.nDaysChallenge.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DajimCommentRequestDto {

    Long dajimNumber; //댓글 단 다짐 넘버

    String nickname; //댓글 작성자 닉네임

    String content; //댓글 내용

}
