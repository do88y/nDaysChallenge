package challenge.nDaysChallenge.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FindFriendsRequestDTO {

    private String id;

    private String nickname;

    private int image;


}
