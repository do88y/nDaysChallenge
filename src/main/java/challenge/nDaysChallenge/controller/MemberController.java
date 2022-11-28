package challenge.nDaysChallenge.controller;


import challenge.nDaysChallenge.dto.request.MemberSignUpDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.service.MemberServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/auth")

@RestController
public class MemberController {

    private final MemberServiceIn memberServiceIn;
    private final MemberRepository memberRepository;


    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.OK)
    public String join(@Valid @RequestBody MemberSignUpDto signUpDto)throws Exception{
        return memberServiceIn.signUp(signUpDto);
    }

}
