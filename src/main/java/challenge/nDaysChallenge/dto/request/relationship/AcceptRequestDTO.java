package challenge.nDaysChallenge.dto.request.relationship;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class AcceptRequestDTO {

    private String  id;                   //친구아이디


    @DateTimeFormat(pattern = "yyyy.mm.dd HH:mm:ss")
    private LocalDateTime acceptedDate;    //요청수락날짜시간



    @Builder
    public AcceptRequestDTO( String id, LocalDateTime acceptedDate){
        this.id=id;
        this.acceptedDate=acceptedDate;

    }
}
