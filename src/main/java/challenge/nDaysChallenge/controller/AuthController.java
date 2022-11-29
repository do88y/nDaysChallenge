package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.dto.TokenDto;
import challenge.nDaysChallenge.dto.request.JwtRequestDto;
import challenge.nDaysChallenge.dto.request.SignupDto;
import challenge.nDaysChallenge.dto.response.MemberResponseDto;
import challenge.nDaysChallenge.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController { //회원가입 & 로그인 & 토큰 재발급

    private final AuthService authService;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody SignupDto signupDto){
        MemberResponseDto memberResponseDto = authService.signUp(signupDto);
        return ResponseEntity.ok(memberResponseDto);

    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login (@RequestBody SignupDto signupDto){
        //파라미터 객체 안에 id, pw 있음
        TokenDto tokenDto = authService.login(signupDto);
        return ResponseEntity.ok(tokenDto);
    }

    //토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue (@RequestBody JwtRequestDto jwtRequestDto){
        //파라미터 객체 안에 access, refresh 토큰 있음
        TokenDto tokenDto = authService.reissue(jwtRequestDto);
        return ResponseEntity.ok(tokenDto);
    }

}
