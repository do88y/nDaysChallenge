package challenge.nDaysChallenge.dto.request.Room;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteRoomRequestDto {

    List<Long> numbers = new ArrayList<>();

    public DeleteRoomRequestDto(List<Long> numbers) {
        this.numbers.addAll(numbers);
    }
}
