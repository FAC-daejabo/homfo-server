package com.homfo.sms.entity;

import com.homfo.error.RequestLimitException;
import com.homfo.sms.command.ValidateSmsCodeCommand;
import com.homfo.sms.infra.enums.SmsCodeStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

 class SmsCodeTest {
    @Test
    @DisplayName("5회까지 새로운 코드 생성")
    void createCode_NewCodeGenerated() {
        // Given
        MockSmsCode smsCode = new MockSmsCode();

        // When
        for (int i = 0; i < SmsCode.REQUEST_LIMIT; i++) {
            smsCode.createCode();
        }

        // Then
        assertNotNull(smsCode.getCode());
        assertEquals(smsCode.getCode().length(), SmsCode.CODE_LENGTH);
        assertEquals(smsCode.getCount(), SmsCode.REQUEST_LIMIT);
        assertEquals(SmsCodeStatus.REQUESTED, smsCode.getStatus());
    }

    @Test
    @DisplayName("5분 내에 여러 번 인증 코드 요청하면 그만큼 count 증가")
    void createCode_NewCodeGenerated_IncreaseCount() {
        // Given
        MockSmsCode smsCode = new MockSmsCode();
        int currentCount = 1;
        int expectCount = currentCount + 1;

        smsCode.setCount(currentCount);
        smsCode.setCreatedAt(LocalDateTime.now());

        // When
        smsCode.createCode();

        // Then
        assertNotNull(smsCode.getCode());
        assertEquals(SmsCode.CODE_LENGTH, smsCode.getCode().length());
        assertEquals(expectCount, smsCode.getCount());
        assertEquals(SmsCodeStatus.REQUESTED, smsCode.getStatus());
    }

    @Test
    @DisplayName("5분 내로 요청 제한 횟수에 도달했다면 에러 발생")
    void createCode_ThrowsRequestLimitException_WhenNotExpired() {
        // Given
        MockSmsCode smsCode = new MockSmsCode();
        int currentCount = 5;

        smsCode.setCount(currentCount);
        smsCode.setCreatedAt(LocalDateTime.now().minusMinutes(4)); // 만료 시간을 설정하여, count가 초기화되도록 함

        // When
        Executable result = smsCode::createCode;

        // Then
        assertThrows(RequestLimitException.class, result);
    }

    @Test
    @DisplayName("요청 제한 횟수에 도달했어도 5분이 지났으면 새로운 코드 생성")
    void createCode_NewCodeGenerated_WhenExpired() {
        // Given
        MockSmsCode smsCode = new MockSmsCode();
        int currentCount = 6;
        int expectCount = 1;

        smsCode.setCount(currentCount);
        smsCode.setCreatedAt(LocalDateTime.now().minusMinutes(6)); // 만료 시간을 설정하여, count가 초기화되도록 함

        // When
        smsCode.createCode();

        // Then
        assertNotNull(smsCode.getCode());
        assertEquals(SmsCode.CODE_LENGTH, smsCode.getCode().length());
        assertEquals(expectCount, smsCode.getCount());
        assertEquals(SmsCodeStatus.REQUESTED, smsCode.getStatus());
    }

    @Test
    @DisplayName("코드 검증 성공")
    void verifyCode_Success() {
        // Given
        MockSmsCode smsCode = new MockSmsCode();
        String givenPhoneNumber = "01012345679";
        String givenCode = "123456";

        smsCode.setPhoneNumber(givenPhoneNumber);
        smsCode.setCode(givenCode);
        smsCode.setCreatedAt(LocalDateTime.now());
        smsCode.setCount(0);

        ValidateSmsCodeCommand command = new ValidateSmsCodeCommand(givenPhoneNumber, givenCode);

        // When
        boolean result = smsCode.verifyCode(command);

        // Then
        assertTrue(result);
        assertEquals(SmsCodeStatus.SUCCESS, smsCode.getStatus());
    }

    @Test
    @DisplayName("요청 제한 횟수에 도달했어도 코드 검증 성공")
    void verifyCode_Success_WhenRequestLimit() {
        // Given
        MockSmsCode smsCode = new MockSmsCode();
        String givenPhoneNumber = "01012345679";
        String givenCode = "123456";

        smsCode.setPhoneNumber(givenPhoneNumber);
        smsCode.setCode(givenCode);
        smsCode.setCreatedAt(LocalDateTime.now());
        smsCode.setCount(SmsCode.REQUEST_LIMIT);

        ValidateSmsCodeCommand command = new ValidateSmsCodeCommand(givenPhoneNumber, givenCode);

        // When
        boolean result = smsCode.verifyCode(command);

        // Then
        assertTrue(result);
        assertEquals(SmsCodeStatus.SUCCESS, smsCode.getStatus());
    }

    @Test
    @DisplayName("전화번호가 달라서 코드 검증 실패")
    void verifyCode_Fail_DifferentPhoneNumber() {
        // Given
        MockSmsCode smsCode = new MockSmsCode();
        String givenPhoneNumber = "01012345679";
        String commandPhoneNumber = "01012345678";
        String givenCode = "123456";

        smsCode.setPhoneNumber(givenPhoneNumber);
        smsCode.setCode(givenCode);
        smsCode.setCreatedAt(LocalDateTime.now());
        smsCode.setCount(0);

        ValidateSmsCodeCommand command = new ValidateSmsCodeCommand(commandPhoneNumber, givenCode);

        // When
        boolean result = smsCode.verifyCode(command);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("코드가 달라서 코드 검증 실패")
    void verifyCode_Fail_DifferentCode() {
        // Given
        MockSmsCode smsCode = new MockSmsCode();
        String givenPhoneNumber = "01012345679";
        String givenCode = "123456";
        String commandCode = "123457";

        smsCode.setPhoneNumber(givenPhoneNumber);
        smsCode.setCode(givenCode);
        smsCode.setCreatedAt(LocalDateTime.now());
        smsCode.setCount(0);

        ValidateSmsCodeCommand command = new ValidateSmsCodeCommand(givenPhoneNumber, commandCode);

        // When
        boolean result = smsCode.verifyCode(command);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("요청 제한 힛수를 넘어서 코드 검증 실패")
    void verifyCode_Fail_RequestLimit() {
        // Given
        MockSmsCode smsCode = new MockSmsCode();
        String givenPhoneNumber = "01012345679";
        String givenCode = "123456";

        smsCode.setPhoneNumber(givenPhoneNumber);
        smsCode.setCode(givenCode);
        smsCode.setCreatedAt(LocalDateTime.now());
        smsCode.setCount(SmsCode.REQUEST_LIMIT + 1);

        ValidateSmsCodeCommand command = new ValidateSmsCodeCommand(givenPhoneNumber, givenCode);

        // When
        boolean result = smsCode.verifyCode(command);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("요청 제한 시간을 지나서 코드 검증 실패")
    void verifyCode_Fail_Expired() {
        // Given
        MockSmsCode smsCode = new MockSmsCode();
        String givenPhoneNumber = "01012345679";
        String givenCode = "123456";

        smsCode.setPhoneNumber(givenPhoneNumber);
        smsCode.setCode(givenCode);
        smsCode.setCreatedAt(LocalDateTime.now().minusMinutes(5));
        smsCode.setCount(SmsCode.REQUEST_LIMIT);

        ValidateSmsCodeCommand command = new ValidateSmsCodeCommand(givenPhoneNumber, givenCode);

        // When
        boolean result = smsCode.verifyCode(command);

        // Then
        assertFalse(result);
    }

    @Test
    @DisplayName("코드 만료 확인")
    void isExpired_CodeExpired() {
        // Given
        MockSmsCode smsCode = new MockSmsCode();

        smsCode.setCreatedAt(LocalDateTime.now().minusMinutes(6)); // 5분보다 오래된 시간 설정

        // When
        boolean result = smsCode.isExpired();

        // Then
        assertTrue(result);
    }

    @Test
    @DisplayName("5분 내면 코드 만료가 되지 않음")
    void isExpired_NotExpired() {
        // Given
        MockSmsCode smsCode = new MockSmsCode();

        smsCode.setCreatedAt(LocalDateTime.now().minusSeconds(SmsCode.EXPIRED_MINUTES * 60 - 1)); // 5분보다 오래된 시간 설정

        // When
        boolean result = smsCode.isExpired();

        // Then
        assertFalse(result);
    }
}