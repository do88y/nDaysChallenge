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


    //view에 넘겨야하는건 string이어야 하니까 dto에서는 string으로
    public List<String> friendsList = AcceptRequestDTO.builder().friendsList;


    @Builder
    public AcceptRequestDTO( LocalDateTime acceptedDate, List<String >friendsList){
        this.id=id;
        this.acceptedDate=acceptedDate;
        for (String friend: friendsList) {
            this.friendsList.add(friend);
        }
    }
}
