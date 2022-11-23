package challenge.nDaysChallenge.controller;


import challenge.nDaysChallenge.domain.MemberSignUpDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import challenge.nDaysChallenge.service.MemberServiceIn;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
<<<<<<< HEAD
@RequestMapping("/auth")
=======
@RequestMapping("/member")
>>>>>>> refs/remotes/origin/develop
@RestController
public class MemberController {

    private final MemberServiceIn memberServiceIn;
    private final MemberRepository memberRepository;

<<<<<<< HEAD
    @PostMapping("/signup")
=======
    @PostMapping("/auth/signup")
>>>>>>> refs/remotes/origin/develop
    @ResponseStatus(HttpStatus.OK)
    public String join(@Valid @RequestBody MemberSignUpDto signUpDto)throws Exception{
        return memberServiceIn.signUp(signUpDto);
    }

}
