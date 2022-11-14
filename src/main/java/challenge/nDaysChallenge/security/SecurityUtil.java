package challenge.nDaysChallenge.security;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    private SecurityUtil(){}

    //시큐리티컨텍스트에 저장된 유저의 username 리턴
    public static Long getCurrentMemberId(){
        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null){
            throw new RuntimeException("시큐리티 컨텍스트에 인증 정보가 없습니다");
        }

        return Long.parseLong(authentication.getName());
    }

}
