package challenge.nDaysChallenge.dto.response.relationship;

import challenge.nDaysChallenge.domain.Relationship;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class AfterDeleteResponseDTO {
    //빈 객체//
    private List<Relationship> friendList =new ArrayList<>();

    //생성자//
    public AfterDeleteResponseDTO (List<Relationship> friendList){
        //값을 넣어줘야해(하나하나씩 꽂아넣어주는 거)//
        this.friendList.addAll(friendList);
    }



}

