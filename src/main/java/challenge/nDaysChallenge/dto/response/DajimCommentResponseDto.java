package challenge.nDaysChallenge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DajimCommentResponseDto {

    Long number; //댓글 넘버

    Long level;//댓글 계층 (0: 댓글, 1:답댓글)

    Long replyOf; //어떤 댓글넘버의 답댓글인지

    Long dajimNumber; //댓글 단 다짐 넘버

    String nickname; //댓글 작성자 닉네임

    String content; //댓글 내용

}
