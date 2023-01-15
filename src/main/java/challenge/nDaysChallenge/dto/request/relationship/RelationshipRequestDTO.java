package challenge.nDaysChallenge.dto.request.relationship;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RelationshipRequestDTO {

  private String  id;                   //친구아이디


  public RelationshipRequestDTO(String id){
    this.id=id;

  }
}