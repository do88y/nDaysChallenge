package challenge.nDaysChallenge.controller;


import challenge.nDaysChallenge.domain.MemberSignUpDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.service.MemberServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RequestMapping("/auth/signup")
@RestController
public class MemberController {

    private final MemberServiceIn memberServiceIn;
    private final MemberRepository memberRepository;

    @ResponseStatus(HttpStatus.OK)
    public String join(@Valid @RequestBody MemberSignUpDto signUpDto)throws Exception{
        return memberServiceIn.signUp(signUpDto);
    }

}
