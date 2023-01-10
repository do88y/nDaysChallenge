package challenge.nDaysChallenge.dto.response.relationship;

import challenge.nDaysChallenge.dto.request.relationship.AcceptRequestDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class AcceptResponseDTO {

    private String  id;                   //친구아이디
    private String nickname;       //친구 닉네임

    private int image;                  //친구 프로필사진

    private String relationshipStatus;

    @DateTimeFormat(pattern = "yyyy.mm.dd HH:mm:ss")
    private LocalDateTime acceptedDate;    //요청수락날짜시간



    @Builder
    public AcceptResponseDTO(String id, String nickname, int image, String relationshipStatus, LocalDateTime acceptedDate){
        this.id=id;
        this.nickname=nickname;
        this.image=image;
        this.relationshipStatus=relationshipStatus;
        this.acceptedDate=acceptedDate;
    }
}
