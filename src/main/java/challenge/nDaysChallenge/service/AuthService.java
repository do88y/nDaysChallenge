package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.dto.JwtDTO;
import challenge.nDaysChallenge.dto.request.JwtRequestDTO;
import challenge.nDaysChallenge.dto.request.MemberRequestDto;
import challenge.nDaysChallenge.jwt.JwtProvider;
import challenge.nDaysChallenge.jwt.RefreshToken;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService { //회원가입 & 로그인 & 토큰 재발급

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

//    @Transactional
//    public Object signup(MemberRequestDto memberRequestDto) {
//    }

    @Transactional
    public JwtDTO login(MemberRequestDto memberRequestDto) {
        //로그인 id, pw 기반으로 authenticationToken (인증 객체) 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        //사용자 비밀번호 검증
        //CustomUserDetailsService의 loadUserByUsername 메소드 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //인증 정보 기반으로 access 토큰 생성
        JwtDTO jwtToken = jwtProvider.generateToken(authentication);

        //refresh 토큰 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(jwtToken.getRefreshToken())
                .build();
        refreshTokenRepository.save(refreshToken);

        //토큰 발급
        return jwtToken;

    }

    @Transactional
    public JwtDTO reissue(JwtRequestDTO tokenRequestDto) {

        //refresh 토큰 유효성(만료 여부) 검증
        if (!jwtProvider.validateToken(tokenRequestDto.getRefreshToken())){
            throw new RuntimeException("리프레시 토큰이 유효하지 않습니다");
        }

        //access 토큰에서 id 가져오기
        Authentication authentication = jwtProvider.getAuthentication(tokenRequestDto.getAccessToken());

        //id 기반으로 저장소에서 refresh 토큰 값 가져오기
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃된 사용자입니다"));

        //refresh 토큰 일치(저장소 - 파라미터) 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())){
            throw new RuntimeException("토큰의 사용자 정보가 일치하지 않습니다");
        }

        //토큰 생성
        JwtDTO jwtToken = jwtProvider.generateToken(authentication);

        //저장소에 새 리프레시 토큰 저장
        RefreshToken newRefreshToken = refreshToken.updateValue(jwtToken.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        //access 토큰 재발급
        return jwtToken;

    }
}
