package challenge.nDaysChallenge.jwt;

import challenge.nDaysChallenge.domain.dajim.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@RedisHash(value = "refreshToken", timeToLive = 1000 * 60 * 60 * 24 * 7) //7일
@Getter
public class RefreshToken { //db에 저장

    @Id
    private String id;

    private String token;

    public RefreshToken(String id, String token){
        this.id=id;
        this.token=token;
    }

}
