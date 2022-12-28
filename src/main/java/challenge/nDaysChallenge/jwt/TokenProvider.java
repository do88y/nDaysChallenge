package challenge.nDaysChallenge.jwt;

import challenge.nDaysChallenge.dto.TokenDto;
import challenge.nDaysChallenge.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider { //유저 정보로 JWT 토큰 생성 & 토큰 통해 유저 정보 가져옴

    private final CustomUserDetailsService customUserDetailsService;

    private static final String AUTHORITY = "auth";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30; // 30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 7일

    private final Key key;

    //키값 세팅
    public TokenProvider(@Value("${jwt.secret}") String secretKey, CustomUserDetailsService customUserDetailsService){
        this.customUserDetailsService = customUserDetailsService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //토큰 생성
    public TokenDto generateToken(Authentication authentication){
        //권한들 가져오기 (문자열 변환)
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Long now = (new Date()).getTime();

        //access token 생성
        Date accessTokenExpireTime = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName()) //페이로드 "sub":"이름"
                .claim(AUTHORITY, authorities) //페이로드 "auth":"ROLE_USER"
                .setExpiration(accessTokenExpireTime) //페이로드 "exp":만료시간
                .signWith(key, SignatureAlgorithm.HS256) //헤더 "alg":"HS512"
                .compact();

        //refresh token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return TokenDto.builder()
                .type(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime.getTime())
                .refreshToken(refreshToken)
                .build();

    }

    public TokenDto reissueToken(Authentication authentication, String refreshToken) {
        //권한들 가져오기 (문자열 변환)
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Long now = (new Date()).getTime();

        //access token 생성
        Date accessTokenExpireTime = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName()) //페이로드 "sub":"이름"
                .claim(AUTHORITY, authorities) //페이로드 "auth":"ROLE_USER"
                .setExpiration(accessTokenExpireTime) //페이로드 "exp":만료시간
                .signWith(key, SignatureAlgorithm.HS256) //헤더 "alg":"HS512"
                .compact();

        return TokenDto.builder()
                .type(BEARER_TYPE)
                .accessToken(accessToken)
                .accessTokenExpireTime(accessTokenExpireTime.getTime())
                .refreshToken(refreshToken)
                .build();
    }

    //토큰 내 데이터(클레임) 가져오기
    public Authentication getAuthentication(String accessToken){
        //토큰 복호화(읽기)
        Claims claims = parseClaims(accessToken);

        //클레임에 권한 정보 없으면 예외 throw
        if (claims.get(AUTHORITY)==null){
            throw new RuntimeException("권한 정보가 없는 토큰입니다");
        }

        //클레임에서 권한 목록 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

//        //클레임 정보로 User 객체 생성
//        UserDetails principal = new User(claims.getSubject(),"", authorities);
//
//        //username, password 형태 인증 위한 객체 생성, 리턴
//        return new UsernamePasswordAuthenticationToken(principal,"", authorities);

//        String id = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(accessToken).getBody().getSubject();//토큰 내 아이디 추출

        log.info("아이디 : " + claims.getSubject());

        UserDetails memberAdapter = customUserDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(memberAdapter,"",authorities);

    }

    //토큰 읽고 검증하기
    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()  //JwtParserBuilder 객체 생성
                    .setSigningKey(key) //서명 증명에 사용할 key
                    .build() //안전한 JwtParser 리턴 위함
                    .parseClaimsJws(token); //해당 토큰 클레임 읽음
            return true;
        } catch (io.jsonwebtoken.security.SecurityException| MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다");
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT 토큰입니다");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰에 오류가 발생했습니다");
        }

        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts
                    .parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(accessToken).getBody(); //페이로드 정보 리턴
        } catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

}
