package challenge.nDaysChallenge.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@Getter
@NoArgsConstructor
public class RelationshipResponseDTO {

  private String id;                   //친구아이디
  private String nickname;     //친구 닉네임

  private int image;                //친구 프로필사진

  @JsonFormat(pattern ="yyyy.mm.dd HH:mm:ss")
  private LocalDateTime acceptedDate;     //수락날짜시간

  //view에 넘겨야하는건 string이어야 하니까 dto에서는 string으로
  private String relationshipStatus;
  public List<String> friendsList = Collections.emptyList();


@Builder
  public RelationshipResponseDTO(String id, String nickname, int image, LocalDateTime acceptedDate, String relationshipStatus, List<String >friendsList){
    this.id=id;
    this.nickname=nickname;
    this.image=image;
    this.acceptedDate=acceptedDate;
    this.relationshipStatus=relationshipStatus;
    this.friendsList=friendsList;
  }
}
