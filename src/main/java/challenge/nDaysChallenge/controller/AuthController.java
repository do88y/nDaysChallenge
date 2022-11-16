package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.controller.dto.JwtDTO;
import challenge.nDaysChallenge.controller.dto.JwtRequestDTO;
import challenge.nDaysChallenge.controller.dto.MemberRequestDto;
import challenge.nDaysChallenge.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController { //회원가입 & 로그인 & 토큰 재발급

    private final AuthService authService;

//    @PostMapping("/signup")
//    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto){
//        return ResponseEntity.ok(authService.signup(memberRequestDto));
//    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login (@RequestBody MemberRequestDto memberRequestDto){ //파라미터 객체 안에 id, pw 있음
        return ResponseEntity.ok(authService.login(memberRequestDto));
    }

    //재발급
    @PostMapping("/reissue")
    public ResponseEntity<JwtDTO> reissue (@RequestBody JwtRequestDTO jwtRequestDto){ //파라미터 객체 안에 access, refresh 토큰 있음
        return ResponseEntity.ok(authService.reissue(jwtRequestDto));
    }

}