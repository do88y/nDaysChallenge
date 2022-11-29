package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.dto.TokenDto;
import challenge.nDaysChallenge.dto.request.JwtRequestDto;
<<<<<<< HEAD
import challenge.nDaysChallenge.dto.request.SignupDto;
=======
import challenge.nDaysChallenge.dto.request.MemberRequestDto;
>>>>>>> cedc1d880e101d6df25ed2baaf8f9f2d210d442d
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
<<<<<<< HEAD
    public ResponseEntity<MemberResponseDto> signup(@RequestBody SignupDto signupDto){
        MemberResponseDto memberResponseDto = authService.signUp(signupDto);
        return ResponseEntity.ok(memberResponseDto);
=======
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto){
        return ResponseEntity.ok(authService.signUp(memberRequestDto));
>>>>>>> cedc1d880e101d6df25ed2baaf8f9f2d210d442d
    }

    //로그인
    @PostMapping("/login")
<<<<<<< HEAD
    public ResponseEntity<TokenDto> login (@RequestBody SignupDto signupDto){
        //파라미터 객체 안에 id, pw 있음
        TokenDto tokenDto = authService.login(signupDto);
        return ResponseEntity.ok(tokenDto);
=======
    public ResponseEntity<TokenDto> login (@RequestBody MemberRequestDto memberRequestDto){
        //파라미터 객체 안에 id, pw 있음
        return ResponseEntity.ok(authService.login(memberRequestDto));
>>>>>>> cedc1d880e101d6df25ed2baaf8f9f2d210d442d
    }

    //토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue (@RequestBody JwtRequestDto jwtRequestDto){
        //파라미터 객체 안에 access, refresh 토큰 있음
<<<<<<< HEAD
        TokenDto tokenDto = authService.reissue(jwtRequestDto);
        return ResponseEntity.ok(tokenDto);
=======
        return ResponseEntity.ok(authService.reissue(jwtRequestDto));
>>>>>>> cedc1d880e101d6df25ed2baaf8f9f2d210d442d
    }

}
