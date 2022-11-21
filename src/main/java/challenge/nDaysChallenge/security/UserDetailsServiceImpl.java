package challenge.nDaysChallenge.security;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

//현재 로그인한 사용자를 db에서 찾아옴
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(id + "님을 찾을 수 없습니다"));

        return new UserDetailsImpl(member);
    }

}
