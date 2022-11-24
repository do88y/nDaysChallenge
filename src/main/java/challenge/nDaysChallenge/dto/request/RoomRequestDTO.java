package challenge.nDaysChallenge.dto.request;

import challenge.nDaysChallenge.domain.room.Category;
import challenge.nDaysChallenge.domain.room.RoomType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RoomRequestDTO {

    private Long room;
    private String name;
    private Long totalDays;
    private Category category;
    private String reward;
    private RoomType type;
    private int passCount;

    private Long member;
    private List<Long> memberList = new ArrayList<>();
}
