package challenge.nDaysChallenge.service.jwt;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.dto.response.jwt.TokenResponseDto;
import challenge.nDaysChallenge.dto.request.jwt.TokenRequestDto;
import challenge.nDaysChallenge.dto.request.jwt.LoginRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.dto.response.member.MemberResponseDto;
import challenge.nDaysChallenge.exception.CustomError;
import challenge.nDaysChallenge.exception.CustomException;
import challenge.nDaysChallenge.exception.ErrorCode;
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
            throw new CustomException(CustomError.EXISTING_ID);
        } else if (memberRepository.existsByNickname(memberRequestDto.getNickname())){
            throw new CustomException(CustomError.EXISTING_NICKNAME);
        }

        //비밀번호 regex 확인 (영문대소문자, 숫자, 특문 하나 이상 포함)
        if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\\\\\\\":{}|<>]).{8,}$",memberRequestDto.getPw())){
            throw new CustomException(CustomError.WRONG_REGEX);
        }

        Member member = memberRequestDto.toMember(passwordEncoder);

        memberRepository.save(member);

        return MemberResponseDto.of(member); //아이디, 닉네임 리턴
    }

    //로그인
    public TokenResponseDto login(LoginRequestDto loginRequestDto) {
        //id, pw 검증
        Member member = memberRepository.findById(loginRequestDto.getId())
                .orElseThrow(()->new CustomException(CustomError.USER_NOT_FOUND));


        if (!passwordEncoder.matches(loginRequestDto.getPw(),member.getPw())){
            throw new CustomException(CustomError.WRONG_PASSWORD);
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
                .orElseThrow(() -> new CustomException(CustomError.TOKEN_NOT_FOUND));

        refreshTokenRepository.delete(refreshToken); //리프레쉬 토큰 삭제

        SecurityContextHolder.clearContext(); // 시큐리티 컨텍스트에서 인증 정보 삭제
    }
    
    //액세스토큰 재발급
    public TokenResponseDto reissue(TokenRequestDto tokenRequestDto, String id) {

        //refresh 토큰 유효성(만료 여부) 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken()).equals("true")){
            if (tokenProvider.validateToken(tokenRequestDto.getRefreshToken()).equals("expired")){
                logout(id);
                throw new CustomException(CustomError.EXPIRED_TOKEN);
            }
            throw new CustomException(CustomError.INVALID_TOKEN);
        }

        //access 토큰에서 id 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        //id 기반으로 저장소에서 refresh 토큰 값 가져오기
        RefreshToken refreshToken = refreshTokenRepository.findById(authentication.getName())
                .orElseThrow(() -> new CustomException(CustomError.USER_LOGOUT));

        //refresh 토큰 일치(저장소 - 파라미터) 검사
        if (!refreshToken.getToken().equals(tokenRequestDto.getRefreshToken())){
            throw new CustomException(CustomError.WRONG_TOKEN);
        }

        //토큰 생성
        TokenResponseDto tokenResponseDto = tokenProvider.reissueToken(authentication, tokenRequestDto.getRefreshToken());

        //JWT 토큰 재발급
        return tokenResponseDto;

    }
}
