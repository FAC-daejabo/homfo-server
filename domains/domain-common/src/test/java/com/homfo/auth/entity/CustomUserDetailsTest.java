package com.homfo.auth.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class CustomUserDetailsTest {

    @Test
    @DisplayName("CustomUserDetails를 인스턴스화 할 때 아래 사항을 검증해야한다.")
    void userDetails_WhenNewInstance_ExpectCorrectBehavior() {
        // given
        Long expectedUserId = 1L;
        CustomUserDetails userDetails = new CustomUserDetails(expectedUserId);

        // when
        Long userId = userDetails.id();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        String password = userDetails.getPassword();
        String username = userDetails.getUsername();
        boolean isAccountNonExpired = userDetails.isAccountNonExpired();
        boolean isAccountNonLocked = userDetails.isAccountNonLocked();
        boolean isCredentialsNonExpired = userDetails.isCredentialsNonExpired();
        boolean isEnabled = userDetails.isEnabled();

        // then
        assertEquals(expectedUserId, userId, "생성 시 제공된 userId와 일치해야 합니다.");
        assertTrue(authorities.isEmpty(), "권한 목록은 비어 있어야 합니다.");
        assertNull(password, "비밀번호는 null이어야 합니다.");
        assertNull(username, "사용자 이름은 null이어야 합니다.");
        assertTrue(isAccountNonExpired, "계정은 만료되지 않았다고 간주되어야 합니다.");
        assertTrue(isAccountNonLocked, "계정은 잠기지 않았다고 간주되어야 합니다.");
        assertTrue(isCredentialsNonExpired, "자격 증명은 만료되지 않았다고 간주되어야 합니다.");
        assertTrue(isEnabled, "계정은 활성화되어 있다고 간주되어야 합니다.");
    }
}
