package challenge.nDaysChallenge.dto.response.relationship;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteResponseDTO {
    private String id;                   //친구아이디
    private String nickname;     //친구 닉네임

    private int image;               //친구 프로필사진

    private String relationshipStatus;



    @Builder
    public DeleteResponseDTO(String id, String nickname, int image, String relationshipStatus){
        this.id=id;
        this.nickname=nickname;
        this.image=image;
        this.relationshipStatus=relationshipStatus;

    }

}

