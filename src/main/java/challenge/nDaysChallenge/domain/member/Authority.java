package challenge.nDaysChallenge.domain.member;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {

    ROLE_USER, ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}