package challenge.nDaysChallenge.service.jwt;

import challenge.nDaysChallenge.domain.member.Member;
import challenge.nDaysChallenge.domain.member.MemberAdapter;
import challenge.nDaysChallenge.exception.CustomError;
import challenge.nDaysChallenge.exception.CustomException;
import challenge.nDaysChallenge.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(CustomError.USER_NOT_FOUND));

        log.info("db에서 Member 객체를 가져왔습니다.");
        log.info("권한 : " + member.getAuthority().toString());

        return new MemberAdapter(member);
    }

}
