package challenge.nDaysChallenge.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Slf4j
public class SecurityUtil {

    private SecurityUtil(){}

    //시큐리티컨텍스트에 저장된 유저의 username 리턴

<<<<<<< HEAD
    public static String getCurrentMemberId() {

        public static String getCurrentMemberId () {
            final Authentication authentication =
                    SecurityContextHolder.getContext().getAuthentication();
=======
    public static String getCurrentMemberId(){
        final Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
>>>>>>> refs/remotes/origin/develop

            if (authentication == null || authentication.getName() == null) {
                throw new RuntimeException("시큐리티 컨텍스트에 인증 정보가 없습니다");
            }

            return authentication.getName();
        }

<<<<<<< HEAD
=======
        return authentication.getName();
>>>>>>> refs/remotes/origin/develop
    }
