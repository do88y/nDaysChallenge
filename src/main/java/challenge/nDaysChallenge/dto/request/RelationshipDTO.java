package challenge.nDaysChallenge.dto.request;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.Relationship;
import challenge.nDaysChallenge.domain.RelationshipStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
public class RelationshipDTO{

  private Long id;                   //친구아이디
  private String nickname;     //친구 닉네임
  private LocalDateTime localDateTime;    //친구 등록된 날짜,시간

  private RelationshipStatus relationshipStatus;
  public List<String> friendsList = Collections.emptyList();


@Builder
  public  RelationshipDTO(Long id, String nickname, LocalDateTime localDateTime, RelationshipStatus relationshipStatus,  List<String >friendsList){
    this.id=id;
    this.nickname=nickname;
    this.localDateTime=localDateTime;
    this.relationshipStatus=relationshipStatus;
    this.friendsList=friendsList;
  }
}
