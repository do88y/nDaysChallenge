package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.MemberSignUpDto;
import org.springframework.transaction.annotation.Transactional;


public interface MemberServiceIn {
    public String signUp(MemberSignUpDto signUpDto) throws Exception;
}

