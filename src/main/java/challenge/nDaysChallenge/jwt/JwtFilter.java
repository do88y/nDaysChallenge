package challenge.nDaysChallenge.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import reactor.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter { //요청당 한번만 거치도록 제한된 Filter 구현체

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    private final TokenProvider tokenProvider;

    //JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext에 저장
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = resolveToken(request); //요청헤더에서 토큰 꺼냄

        //토큰 유효성 검사 -> Authentication 객체 가져와 SecurityContext애 저장
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)){ //토큰 검증
            Authentication authentication = tokenProvider.getAuthentication(jwt); //해당 토큰의 사용자 정보 객체 가져옴
            SecurityContextHolder.getContext().setAuthentication(authentication); //시큐리티컨텍스트에 세팅
        }

        filterChain.doFilter(request,response); //다음 필터로 이동
    }

    //요청 헤더에서 토큰 정보 꺼내옴
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            return bearerToken.substring(7);
        }

        return null;
    }

}