package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberSignUpDto;
import challenge.nDaysChallenge.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class MemberService implements MemberServiceIn {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public String signUp(MemberSignUpDto signUpDto) throws Exception {

        if (memberRepository.existsById(signUpDto.getId()) == true) {
            throw new Exception("이미 존재하는 이메일입니다.");
        }

        if (memberRepository.existsByNickname(signUpDto.getNickname()) == true) {
            throw new Exception("이미 존재하는 닉네임입니다.");
        }

        Member member = memberRepository.save(signUpDto.memberToEntity());

        member.addUserAuthority();
        return signUpDto.getId();
    }


}


