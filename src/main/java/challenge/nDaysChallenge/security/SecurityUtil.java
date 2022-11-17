package challenge.nDaysChallenge.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    private SecurityUtil(){}

    //시큐리티컨텍스트에 저장된 유저의 username 리턴
<<<<<<< HEAD

    public static long getCurrentMemberId(){
=======
    public static String getCurrentMemberId(){
>>>>>>> 343006a52ce06dbbd3998f7c7e339a9e4bf36f5b
        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getName() == null){
            throw new RuntimeException("시큐리티 컨텍스트에 인증 정보가 없습니다");
        }

        return authentication.getName();
    }

}
