package challenge.nDaysChallenge.jwt;

import challenge.nDaysChallenge.domain.dajim.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken { //db에 저장

    @Id
    @Column(name = "rt_key")
    private String key;

    @Column(name = "rt_value")
    private String value;

    @Column
    private LocalDateTime createdDate;

    @Builder
    public RefreshToken(String key, String value){
        this.key=key;
        this.value=value;
        this.createdDate=LocalDateTime.now();
    }

}
