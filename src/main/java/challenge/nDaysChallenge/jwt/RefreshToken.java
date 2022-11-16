package challenge.nDaysChallenge.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken { //리프레시 토큰은 db에 저장

    @Id
    @Column(name = "rt_key")
    private String key;

    @Column(name = "rt_value")
    private String value;

    @Builder
    public RefreshToken(String key, String value){
        this.key=key;
        this.value=value;
    }

    public RefreshToken updateValue(String token){
        this.value=token;
        return this;
    }

}
