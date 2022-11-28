package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.dto.request.MemberSignUpDto;


public interface MemberServiceIn {
    public String signUp(MemberSignUpDto signUpDto) throws Exception;
}

