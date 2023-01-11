package challenge.nDaysChallenge.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FindFriendsRequestDTO {

    private String id;

    private String nickname;

    private int image;


}
