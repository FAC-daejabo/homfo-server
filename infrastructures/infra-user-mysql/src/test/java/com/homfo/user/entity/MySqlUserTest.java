package com.homfo.user.entity;

import com.homfo.enums.Gender;
import com.homfo.user.infra.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class MySqlUserTest {

    private MySqlUser user;
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        user = MySqlUser.builder()
                .account("testUser")
                .password(encoder.encode("Password@123"))
                .phoneNumber("010-1234-5678")
                .nickname("nickname")
                .gender(Gender.MAN)
                .birthday(LocalDate.of(1990, 1, 1))
                .job("Developer")
                .build();
    }

    @Test
    void signIn_WithCorrectPassword_ShouldSucceed() {
        // given: 올바른 비밀번호 준비
        String correctPassword = "Password@123";

        // when: signIn 메서드 실행
        Executable action = () -> user.signIn(encoder, correctPassword);

        // then: 예외가 발생하지 않아야 함
        assertDoesNotThrow(action);
    }

    @Test
    void signIn_WithIncorrectPassword_ShouldFail() {
        // given: 잘못된 비밀번호 준비
        String wrongPassword = "wrongPassword";

        // when: signIn 메서드 실행
        Executable action = () -> user.signIn(encoder, wrongPassword);

        // then: RuntimeException 예외 발생
        assertThrows(RuntimeException.class, action);
    }

    @Test
    void deleteAccount_ShouldSetUserStatusToDeleted() {
        // given: 사용자 계정 존재

        // when: deleteAccount 메서드 실행
        user.deleteAccount();

        // then: 사용자 상태가 DELETED로 변경되고, 계정 관련 정보가 null이 되어야 함
        assertEquals(UserStatus.DELETED, user.getStatus());
        assertNull(user.getAccount());
        assertNull(user.getPassword());
        assertNull(user.getPhoneNumber());
        assertNull(user.getNickname());
    }
}
