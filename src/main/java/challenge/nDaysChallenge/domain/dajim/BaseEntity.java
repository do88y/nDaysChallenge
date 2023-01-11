package challenge.nDaysChallenge.domain.dajim;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class BaseEntity {

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist //persist 직전
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDate = now;
        updatedDate = now;
    }

    @PreUpdate //update 실행 직전
    public void preUpdate(){
        updatedDate = LocalDateTime.now();
    }

}
