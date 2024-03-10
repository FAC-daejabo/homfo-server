package com.homfo.employee.infra.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValidationUtilTest {
    @Test
    @DisplayName("유효한 비밀번호가 주어졌을 때, 검증에 통과해야 한다")
    void validateUserOriginPassword_WithValidPassword_ShouldPass() {
        // given
        String validPassword = "Valid123!";

        // when
        Executable validation = () -> ValidationUtil.validateUserOriginPassword(validPassword);

        // then
        assertDoesNotThrow(validation, "올바른 비밀번호는 Exception이 발생하지 않습니다.");
    }

    @Test
    @DisplayName("짧은 비밀번호가 주어졌을 때, IllegalArgumentException이 발생해야 한다.")
    void validateUserOriginPassword_WithShortPassword_ShouldThrowException() {
        // given
        String shortPassword = "short";

        // when
        Executable validation = () -> ValidationUtil.validateUserOriginPassword(shortPassword);

        // then
        assertThrows(IllegalArgumentException.class, validation, "짧은 비밀번호는 IllegalArgumentException이 발생합니다.");
    }

    @Test
    @DisplayName("긴 비밀번호가 주어졌을 때, IllegalArgumentException이 발생해야 한다.")
    void validateUserOriginPassword_WithInvalidPassword_ShouldThrowException() {
        // given
        String longPassword = "shortshortshortshortshort123213@123SSSS";

        // when
        Executable validation = () -> ValidationUtil.validateUserOriginPassword(longPassword);

        // then
        assertThrows(IllegalArgumentException.class, validation, "긴 비밀번호는 IllegalArgumentException이 발생합니다.");
    }

    @Test
    @DisplayName("비밀번호가 null 일 때, IllegalArgumentException이 발생해야 한다.")
    void validateUserOriginPassword_WithNullPassword_ShouldThrowException() {
        // given
        String nullPassword = null;

        // when
        Executable validation = () -> ValidationUtil.validateUserOriginPassword(nullPassword);

        // then
        assertThrows(IllegalArgumentException.class, validation, "비밀번호가 null이면 IllegalArgumentException이 발생합니다.");
    }

    @Test
    @DisplayName("숫자가 없는 비밀번호가 주어졌을 때, IllegalArgumentException이 발생해야 한다.")
    void validateUserOriginPassword_WithPasswordMissingNumber_ShouldThrowException() {
        // given
        String passwordMissingNumber = "ValidPassword!";

        // when
        Executable validation = () -> ValidationUtil.validateUserOriginPassword(passwordMissingNumber);

        // then
        assertThrows(IllegalArgumentException.class, validation, "숫자가 없는 비밀번호는 IllegalArgumentException이 발생합니다.");
    }

    @Test
    @DisplayName("특수문자가 없는 비밀번호가 주어졌을 때, IllegalArgumentException이 발생해야 한다.")
    void validateUserOriginPassword_WithPasswordMissingSpecialCharacter_ShouldThrowException() {
        // given
        String passwordMissingSpecialChar = "Valid123";

        // when
        Executable validation = () -> ValidationUtil.validateUserOriginPassword(passwordMissingSpecialChar);

        // then
        assertThrows(IllegalArgumentException.class, validation, "특수문자가 없는 비밀번호는 IllegalArgumentException이 발생합니다.");
    }

    @Test
    @DisplayName("대문자가 없는 비밀번호가 주어졌을 때, IllegalArgumentException이 발생해야 한다.")
    void validateUserOriginPassword_WithPasswordMissingCapital_ShouldThrowException() {
        // given
        String passwordMissingSpecialChar = "valid123!";

        // when
        Executable validation = () -> ValidationUtil.validateUserOriginPassword(passwordMissingSpecialChar);

        // then
        assertThrows(IllegalArgumentException.class, validation, "대문자가 없는 비밀번호는 IllegalArgumentException이 발생합니다.");
    }
}
