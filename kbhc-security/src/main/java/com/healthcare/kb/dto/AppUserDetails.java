package com.healthcare.kb.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Builder
public class AppUserDetails implements UserDetails {

    @Getter
    private final Long userNo;
    private final String email;
    @Getter
    private final String name;
    private final String password;
    private final String role;
    private final Collection<? extends GrantedAuthority> grantedAuthorities;
    private final String idType;

    public static AppUserDetails valueOf(Long userNo, String email, String name, String role){
        return AppUserDetails.builder()
                .userNo(userNo)
                .email(email)
                .name(name)
                .role(role)
                .build();
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    // 권한부여
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        String[] roleArr = role.split(",");
        for (String r : roleArr) {
            authorities.add(new SimpleGrantedAuthority(r));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return null;
    }
}
