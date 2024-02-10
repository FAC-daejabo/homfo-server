package com.hompo.auth.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

    private Long userId;

    // 필요한 추가 정보들 (예: 사용자 권한)
    // 여기에서는 예시로 권한 관련 정보는 생략하고, 필요한 경우 추가할 수 있습니다.

    public CustomUserDetails(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 권한 관련 정보 반환
        return Collections.emptyList(); // 예시로 빈 리스트 반환
    }

    @Override
    public String getPassword() {
        // 실제 사용하지 않는 경우 null 또는 빈 문자열 반환
        return "";
    }

    @Override
    public String getUsername() {
        // 실제 사용하지 않는 경우 null 또는 빈 문자열 반환
        return "";
    }

    // UserDetails 인터페이스의 다른 메서드들 구현
    // isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled
    // 여기에서는 모두 true를 반환하는 예시 구현을 제공합니다.

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
