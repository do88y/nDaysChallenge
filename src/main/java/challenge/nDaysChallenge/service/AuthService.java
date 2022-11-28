package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.dto.TokenDto;
import challenge.nDaysChallenge.dto.request.JwtRequestDto;
import challenge.nDaysChallenge.dto.request.MemberRequestDto;
import challenge.nDaysChallenge.dto.request.SignupDto;
import challenge.nDaysChallenge.dto.response.MemberResponseDto;
import challenge.nDaysChallenge.jwt.TokenProvider;
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
@Transactional
public class AuthService { //회원가입 & 로그인 & 토큰 재발급

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public MemberResponseDto signUp(SignupDto signupDto) {
        if (memberRepository.existsById(signupDto.getId())) {
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        if (memberRepository.existsByNickname(signupDto.getNickname())) {
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

        Member member = signupDto.toMember(passwordEncoder);

        return MemberResponseDto.of(memberRepository.save(member)); //아이디, 닉네임 리턴
    }

    public TokenDto login(MemberRequestDto memberRequestDto) {
        //로그인 id, pw 기반으로 authenticationToken (인증 객체) 생성
        UsernamePasswordAuthenticationToken authenticationToken = memberRequestDto.toAuthentication();

        //사용자 비밀번호 검증
        //CustomUserDetailsService의 loadUserByUsername 메소드 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        //인증 정보 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        //refresh 토큰 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        //토큰 발급
        return tokenDto;

    }

    public TokenDto reissue(JwtRequestDto tokenRequestDto) {

        //refresh 토큰 유효성(만료 여부) 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())){
            throw new RuntimeException("리프레시 토큰이 유효하지 않습니다");
        }

        //access 토큰에서 id 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        //id 기반으로 저장소에서 refresh 토큰 값 가져오기
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃된 사용자입니다"));

        //refresh 토큰 일치(저장소 - 파라미터) 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())){
            throw new RuntimeException("토큰의 사용자 정보가 일치하지 않습니다");
        }

        //토큰 생성
        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        //저장소에 새 리프레시 토큰 저장
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        //JWT 토큰 재발급
        return tokenDto;

    }
}
