package challenge.nDaysChallenge.controller;

import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.dto.response.jwt.TokenResponseDto;
import challenge.nDaysChallenge.dto.request.jwt.TokenRequestDto;
import challenge.nDaysChallenge.dto.request.jwt.LoginRequestDto;
import challenge.nDaysChallenge.dto.request.member.MemberRequestDto;
import challenge.nDaysChallenge.dto.response.member.MemberResponseDto;
import challenge.nDaysChallenge.service.jwt.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
    public ResponseEntity<String> logout (@AuthenticationPrincipal MemberAdapter memberAdapter){
        String id = memberAdapter.getMember().getId();
        authService.logout(id);
        return ResponseEntity.ok().body(id + " 로그아웃 완료");
    }

    //토큰 재발급
    @PostMapping("/auth/reissue")
    public ResponseEntity<TokenResponseDto> reissue (@RequestBody TokenRequestDto tokenRequestDto, @AuthenticationPrincipal MemberAdapter memberAdapter){
        TokenResponseDto tokenResponseDto = authService.reissue(tokenRequestDto, memberAdapter.getMember().getId());
        return ResponseEntity.ok().body(tokenResponseDto);
    }

}
