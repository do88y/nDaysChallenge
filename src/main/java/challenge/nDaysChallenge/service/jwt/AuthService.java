package challenge.nDaysChallenge.service.jwt;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.dto.response.jwt.TokenResponseDto;
import challenge.nDaysChallenge.dto.request.jwt.TokenRequestDto;
import challenge.nDaysChallenge.dto.request.jwt.LoginRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.dto.response.member.MemberResponseDto;
import challenge.nDaysChallenge.jwt.TokenProvider;
import challenge.nDaysChallenge.jwt.RefreshToken;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import challenge.nDaysChallenge.repository.jwt.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.Valid;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService { //회원가입 & 로그인 & 토큰 재발급

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    //회원가입
    public MemberResponseDto signup(@Valid MemberRequestDto memberRequestDto) {
        if (memberRepository.existsById(memberRequestDto.getId())){
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        } else if (memberRepository.existsByNickname(memberRequestDto.getNickname())){
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }

        //비밀번호 regex 확인 (영문대소문자, 숫자, 특문 하나 이상 포함)
        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\\\\\\\":{}|<>]).{8,}$",memberRequestDto.getPw())){
            throw new RuntimeException("정해진 형식에 맞게 비밀번호를 입력해주세요.");
        }

        Member member = memberRequestDto.toMember(passwordEncoder);

        memberRepository.save(member);

        return MemberResponseDto.of(member); //아이디, 닉네임 리턴
    }

    //로그인
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        //id, pw 검증
        Member member = memberRepository.findById(loginRequestDto.getId())
                .orElseThrow(()->new IllegalArgumentException("가입되지 않은 이메일입니다."));

        if (!passwordEncoder.matches(loginRequestDto.getPw(),member.getPw())){
            throw new IllegalArgumentException("잘못된 비밀번호입니다.");
        }

        //로그인 id, pw 기반으로 authenticationToken (인증 객체) 생성
        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();

        //사용자 비밀번호 검증
        //CustomUserDetailsService의 loadUserByUsername 메소드 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        log.info("authentication name : "+authentication.getName());
        log.info("authentication authorities : "+authentication.getAuthorities());

        //인증 정보 기반으로 JWT 토큰 생성
        TokenResponseDto tokenResponseDto = tokenProvider.generateToken(authentication);

        //refresh 토큰 저장
        RefreshToken refreshToken = new RefreshToken(authentication.getName(), tokenResponseDto.getRefreshToken());

        refreshTokenRepository.save(refreshToken);

        //토큰 발급
        return tokenResponseDto;

    }

    //로그아웃
    public void logout(String id){
        RefreshToken refreshToken = refreshTokenRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("사용자의 리프레시 토큰을 찾을 수 없습니다."));

        refreshTokenRepository.delete(refreshToken); //리프레쉬 토큰 삭제

        SecurityContextHolder.clearContext(); // 시큐리티 컨텍스트에서 인증 정보 삭제
    }
    
    //액세스토큰 재발급
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto, String id) {

        //refresh 토큰 유효성(만료 여부) 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken()).equals("true")){
            if (tokenProvider.validateToken(tokenRequestDto.getRefreshToken()).equals("expired")){
                logout(id);
                throw new RuntimeException("리프레시 토큰이 만료되었습니다");
            }
            throw new RuntimeException("리프레시 토큰이 유효하지 않습니다");
        }

        //access 토큰에서 id 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        //id 기반으로 저장소에서 refresh 토큰 값 가져오기
        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃된 사용자입니다"));

        //refresh 토큰 일치(저장소 - 파라미터) 검사
        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken())){
            throw new RuntimeException("토큰의 사용자 정보가 일치하지 않습니다");
        }

        //토큰 생성
        TokenResponseDto tokenResponseDto = tokenProvider.reissueToken(authentication, tokenRequestDto.getRefreshToken());

        //JWT 토큰 재발급
        return tokenResponseDto;

    }
}
