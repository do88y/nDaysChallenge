package challenge.nDaysChallenge.service;

import challenge.nDaysChallenge.domain.Member;
import challenge.nDaysChallenge.domain.MemberAdapter;
import challenge.nDaysChallenge.repository.MemberRepository;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    //db 통해 찾은 User - Authentication 간 비밀번호 비교, 검증
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Member member = memberRepository.findById(id)
//                .map(this::createUser) //현 클래스의 createUserDetails 메소드와 매핑
                .orElseThrow(() -> new UsernameNotFoundException("db에서 " + id + " 사용자를 찾을 수 없습니다"));

        return new MemberAdapter(member);
    }

//    //db에 사용자 값 있을 때 UserDetails 객체 리턴
//    private User createUser(Member member){
//        GrantedAuthority authority = new SimpleGrantedAuthority(member.getAuthority().toString());
//
//        return new User(
//                member.getId(),
//                member.getPw(),
//                Collections.singleton(authority)
//        );
//    }

}
