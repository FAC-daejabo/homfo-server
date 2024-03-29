package com.homfo.user.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("올바른 계정이 주어졌을 때, 검증에 통과해야 한다")
    void validateAccount_WithValidAccount_ShouldPass() {
        // given
        User user = new MockUser();
        String validAccount = "ValidAccount1";

        // when
        Executable validation = () -> user.validateAccount(validAccount);

        // then
        assertDoesNotThrow(validation);
    }

    @Test
    @DisplayName("올바르지 않은 계정이 주어졌을 때, IllegalArgumentException이 발생해야 한다.")
    void validateAccount_WithInvalidAccount_ShouldThrowException() {
        // given
        User user = new MockUser();
        String invalidAccount = "inv";

        // when
        Executable validation = () -> user.validateAccount(invalidAccount);

        // then
        assertThrows(IllegalArgumentException.class, validation);
    }

    @Test
    @DisplayName("올바른 비밀번호가 주어졌을 때, 검증에 통과해야 한다")
    void validatePassword_WithValidPassword_ShouldPass() {
        // given
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new MockUser();
        String validPassword = encoder.encode("ValidPass123!");

        // when
        Executable validation = () -> user.validatePassword(validPassword);

        // then
        assertDoesNotThrow(validation);
    }

    @Test
    @DisplayName("암호화 규격에 맞지 않는 비밀번호가 주어졌을 때, IllegalArgumentException이 발생해야 한다.")
    void validatePassword_WithInvalidPassword_ShouldThrowException() {
        // given
        User user = new MockUser();
        String invalidPassword = "invalid";

        // when
        Executable validation = () -> user.validatePassword(invalidPassword);

        // then
        assertThrows(IllegalArgumentException.class, validation);
    }

    @Test
    @DisplayName("올바른 닉네임이 주어졌을 때, 검증에 통과해야 한다")
    void validateNickname_WithValidNickname_ShouldPass() {
        // given
        User user = new MockUser();
        String validNickname = "ValidNick1";

        // when
        Executable validation = () -> user.validateNickname(validNickname);

        // then
        assertDoesNotThrow(validation);
    }

    @Test
    @DisplayName("올바르지 않은 닉네임이 주어졌을 때, IllegalArgumentException이 발생해야 한다")
    void validateNickname_WithInvalidNickname_ShouldThrowException() {
        // given
        User user = new MockUser();
        String validNickname = "가나다ㄹ";

        // when
        Executable validation = () -> user.validateNickname(validNickname);

        // then
        assertThrows(IllegalArgumentException.class, validation);
    }

    @Test
    @DisplayName("올바른 전화번호가 주어졌을 때, 검증에 통과해야 한다")
    void validatePhoneNumber_WithValidPhoneNumber_ShouldPass() {
        // given
        User user = new MockUser();
        String validPhoneNumber = "010-1234-5678";

        // when
        Executable validation = () -> user.validatePhoneNumber(validPhoneNumber);

        // then
        assertDoesNotThrow(validation);
    }

    @Test
    @DisplayName("올바르지 않은 전화번호가 주어졌을 때, IllegalArgumentException이 발생해야 한다")
    void validatePhoneNumber_WithInvalidPhoneNumber_ShouldThrowException() {
        // given
        User user = new MockUser();
        String validPhoneNumber = "0101234-5678";

        // when
        Executable validation = () -> user.validatePhoneNumber(validPhoneNumber);

        // then
        assertThrows(IllegalArgumentException.class, validation);
    }

    @Test
    @DisplayName("올바른 직업이 주어졌을 때, 검증에 통과해야 한다")
    void validateJob_WithValidJob_ShouldPass() {
        // given
        User user = new MockUser();
        String validJob = "대학생";

        // when
        Executable validation = () -> user.validateJob(validJob);

        // then
        assertDoesNotThrow(validation);
    }

    @Test
    @DisplayName("올바르지 않은 직업이 주어졌을 때, IllegalArgumentException이 발생해야 한다")
    void validateJob_WithInvalidJob_ShouldThrowException() {
        // given
        User user = new MockUser();
        String validJob = "#$%%^";

        // when
        Executable validation = () -> user.validateJob(validJob);

        // then
        assertThrows(IllegalArgumentException.class, validation);
    }
}