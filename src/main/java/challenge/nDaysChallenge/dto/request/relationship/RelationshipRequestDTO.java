package challenge.nDaysChallenge.dto.request.relationship;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RelationshipRequestDTO {

  private String  id;                   //친구아이디

  private String nickname;       //친구 닉네임

  private int image;                  //친구 프로필사진

  @DateTimeFormat(pattern = "yyyy-mm-dd HH:mm:ss")
  private LocalDateTime requestedDate;    //요청날짜시간



  public RelationshipRequestDTO(String id, String nickname, int image){
    this.id=id;
    this.nickname=nickname;
    this.image=image;
  }
}