package challenge.nDaysChallenge.dto.request;

import challenge.nDaysChallenge.domain.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
public class RelationshipRequestDTO {

  private String  id;                   //친구아이디
  private String nickname;     //친구 닉네임

  private int image;               //친구 프로필사진

  private String relationshipStatus;


  @DateTimeFormat(pattern = "")
  private LocalDateTime requestedDate;    //요청날짜시간


  //view에 넘겨야하는건 string이어야 하니까 dto에서는 string으로
  public List<String> friendsList = RelationshipRequestDTO.builder().friendsList;


@Builder
  public RelationshipRequestDTO (String id, String nickname, int image, LocalDateTime requestedDate,  String relationshipStatus, List<String >friendsList){
    this.id=id;
    this.nickname=nickname;
    this.image=image;
    this.requestedDate=requestedDate;
    this.relationshipStatus=relationshipStatus;
    this.friendsList=friendsList;
  }
}
