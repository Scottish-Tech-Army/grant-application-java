package com.commongrant.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;

public class OrgUserDetails implements UserDetails {

    private final Long userId;
    private final Long orgId;
    private final String email;
    private final String role;

    public OrgUserDetails(Long userId, Long orgId, String email, String role) {
        this.userId = userId; this.orgId = orgId;
        this.email = email; this.role = role;
    }

    public Long getUserId() { return userId; }
    public Long getOrgId()  { return orgId; }

    @Override public String getUsername() { return email; }
    @Override public String getPassword() { return null; }
    @Override public boolean isAccountNonExpired()  { return true; }
    @Override public boolean isAccountNonLocked()   { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    @Override
    public List<SimpleGrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }
}

