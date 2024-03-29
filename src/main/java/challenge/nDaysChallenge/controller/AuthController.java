package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.dto.response.jwt.TokenResponseDto;
import challenge.nDaysChallenge.dto.request.jwt.TokenRequestDto;
import challenge.nDaysChallenge.dto.request.jwt.LoginRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.dto.response.member.MemberResponseDto;
import challenge.nDaysChallenge.service.jwt.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController { //회원가입 & 로그인 & 토큰 재발급

    private final AuthService authService;

    //회원가입
    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody MemberRequestDto memberRequestDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
           return ResponseEntity.badRequest().body(bindingResult.getFieldErrors());
        }

        MemberResponseDto memberResponseDto = authService.signup(memberRequestDto);

        return ResponseEntity.ok().body(memberResponseDto);
    }

    //로그인
    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponseDto> login (@RequestBody LoginRequestDto loginRequestDto){
        TokenResponseDto tokenResponseDto = authService.login(loginRequestDto);

        return ResponseEntity.ok().body(tokenResponseDto);
    }

    //로그아웃
    @PostMapping("/auth/logout")
    public ResponseEntity<String> logout (Principal principal){
        authService.logout(principal.getName());

        URI location = URI.create("/");

        return ResponseEntity.status(301).location(location).build();
    }

    //토큰 재발급
    @PostMapping("/auth/reissue")
    public ResponseEntity<TokenResponseDto> reissue (@RequestBody TokenRequestDto tokenRequestDto, Principal principal){
        TokenResponseDto tokenResponseDto = authService.reissue(tokenRequestDto, principal.getName());
        return ResponseEntity.ok().body(tokenResponseDto);
    }

}
