package com.hompo.user.infra.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValidationUtilTest {

    @Test
    void validateUserOriginPassword_WithValidPassword_ShouldPass() {
        // Good case: 유효한 비밀번호
        String validPassword = "Password123!";
        assertDoesNotThrow(() -> ValidationUtil.validateUserOriginPassword(validPassword));
    }

    @Test
    void validateUserOriginPassword_WithInvalidPassword_ShouldThrowException() {
        // Bad case: 길이가 짧은 비밀번호
        String shortPassword = "Short1!";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateUserOriginPassword(shortPassword));
        assertTrue(exception.getMessage().contains("최소 8자 이상이며"));

        // Bad case: 길이가 긴 비밀번호
        String longPassword = "LongPassword@123456789";
        exception = assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateUserOriginPassword(longPassword));
        assertTrue(exception.getMessage().contains("최소 8자 이상이며"));

        // Bad case: 숫자가 없는 비밀번호
        String noNumberPassword = "NoNumberPassword!";
        exception = assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateUserOriginPassword(noNumberPassword));
        assertTrue(exception.getMessage().contains("최소 8자 이상이며"));

        // Bad case: 대소문자가 없는 비밀번호
        String noLetterPassword = "12345678!";
        exception = assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateUserOriginPassword(noLetterPassword));
        assertTrue(exception.getMessage().contains("최소 8자 이상이며"));

        // Bad case: 특수문자가 없는 비밀번호
        String noSpecialCharPassword = "NoSpecialChar1";
        exception = assertThrows(IllegalArgumentException.class, () -> ValidationUtil.validateUserOriginPassword(noSpecialCharPassword));
        assertTrue(exception.getMessage().contains("최소 8자 이상이며"));
    }
}
