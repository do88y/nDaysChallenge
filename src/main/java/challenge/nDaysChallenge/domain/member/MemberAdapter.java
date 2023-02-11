package challenge.nDaysChallenge.domain.member;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.*;

public class MemberAdapter extends User {

    private Member member;

    public MemberAdapter(Member member){
        super(member.getId(), member.getPw(), Collections.singleton(member.getAuthority()));
        this.member=member;
    }

    public Member getMember(){
        return member;
    }

}
