package challenge.nDaysChallenge.dto.response.relationship;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class RequestResponseDTO {
    private String id;                   //친구아이디
    private String nickname;     //친구 닉네임

    private int image;               //친구 프로필사진


    @JsonFormat(pattern ="yyyy.mm.dd HH:mm:ss")
    private LocalDateTime requestDate;     //수락날짜시간

    //view에 넘겨야하는건 string이어야 하니까 dto에서는 string으로
    private String relationshipStatus;
    public List<String> friendsList;


    @Builder
    public RequestResponseDTO(String id, String nickname, int image, LocalDateTime requestDate, String relationshipStatus){
        this.id=id;
        this.nickname=nickname;
        this.image=image;
        this.requestDate=requestDate;
        this.relationshipStatus=relationshipStatus;
    }

}
