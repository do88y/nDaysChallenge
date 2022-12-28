package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.dto.TokenDto;
import challenge.nDaysChallenge.dto.request.JwtRequestDto;
import challenge.nDaysChallenge.dto.request.LoginRequestDto;
import challenge.nDaysChallenge.dto.request.MemberRequestDto;
import challenge.nDaysChallenge.dto.response.MemberResponseDto;
import challenge.nDaysChallenge.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController { //회원가입 & 로그인 & 토큰 재발급

    private final AuthService authService;

    //회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<MemberResponseDto> signup(@RequestBody MemberRequestDto memberRequestDto){
        MemberResponseDto memberResponseDto = authService.signup(memberRequestDto);
        return ResponseEntity.ok().body(memberResponseDto);
    }

    //아이디 중복 검사
    @GetMapping("/auth/id-check")
    public ResponseEntity<Boolean> idCheck (@RequestBody String id){
        return ResponseEntity.ok().body(authService.idCheck(id));
    }


    //닉네임 중복 검사
    @GetMapping("/auth/nickname-check")
    public ResponseEntity<Boolean> nicknameCheck (@RequestBody String nickname){
        return ResponseEntity.ok().body(authService.nicknameCheck(nickname));
    }

    //로그인
    @PostMapping("/auth/login")
    public ResponseEntity<TokenDto> login (@RequestBody LoginRequestDto loginRequestDto){
        //파라미터 객체 안에 id, pw 있음
        TokenDto tokenDto = authService.login(loginRequestDto);
        return ResponseEntity.ok().body(tokenDto);
    }

    //로그아웃
    @PostMapping("/auth/logout")
    public void logout (@AuthenticationPrincipal MemberAdapter memberAdapter){
        String id = memberAdapter.getMember().getId();
        authService.logout(id);
    }

    //토큰 재발급
    @PostMapping("/auth/reissue")
    public ResponseEntity<TokenDto> reissue (@RequestBody JwtRequestDto jwtRequestDto, @AuthenticationPrincipal MemberAdapter memberAdapter){
        //파라미터 객체 안에 access, refresh 토큰 있음
        TokenDto tokenDto = authService.reissue(jwtRequestDto, memberAdapter.getMember().getId());
        return ResponseEntity.ok().body(tokenDto);
    }

}
