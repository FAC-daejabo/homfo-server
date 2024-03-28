package com.homfo.user.entity;

import com.homfo.sms.dto.SmsCodeDto;
import com.homfo.sms.dto.SmsCodeTransactionDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JpaUserSmsCodeTest {

    @Test
    @DisplayName("롤백 메소드는 유효한 SmsCodeTransactionDto가 주어지면 상태를 이전 상태로 복원한다")
    void rollback_ValidTransaction_RestoresPreviousState() {
        // Given
        String givenPhoneNumber = "010-1234-5678";
        JpaUserSmsCode smsCode = new JpaUserSmsCode(givenPhoneNumber);
        SmsCodeDto beforeCode = new SmsCodeDto(smsCode.getPhoneNumber(), smsCode.getCode(), smsCode.getStatus(), smsCode.getFirstCreatedAt(), smsCode.getCreatedAt());

        smsCode.createCode();

        SmsCodeDto afterCode = new SmsCodeDto(smsCode.getPhoneNumber(), smsCode.getCode(), smsCode.getStatus(), smsCode.getFirstCreatedAt(), smsCode.getCreatedAt());
        SmsCodeTransactionDto smsCodeTransactionDto = new SmsCodeTransactionDto(
                givenPhoneNumber, // phoneNumber
                beforeCode, // before
                afterCode // after
        );

        // When
        smsCode.rollback(smsCodeTransactionDto);

        // Then
        assertThat(smsCode.getCode()).isEqualTo(beforeCode.code());
        assertThat(smsCode.getStatus()).isEqualTo(beforeCode.status());
    }

    @Test
    @DisplayName("롤백 메소드는 유효하지 않은 SmsCodeTransactionDto가 주어지면 IllegalArgumentException을 발생시킨다")
    void rollback_InvalidTransaction_ThrowsIllegalArgumentException() {
        // Given
        String givenPhoneNumber = "010-1234-5678";
        JpaUserSmsCode smsCode = new JpaUserSmsCode(givenPhoneNumber);
        SmsCodeDto beforeCode = new SmsCodeDto(smsCode.getPhoneNumber(), smsCode.getCode(), smsCode.getStatus(), smsCode.getFirstCreatedAt(), smsCode.getCreatedAt());

        smsCode.createCode();

        SmsCodeDto afterCode = new SmsCodeDto(smsCode.getPhoneNumber(), smsCode.getCode(), smsCode.getStatus(), smsCode.getFirstCreatedAt().plusMinutes(1), smsCode.getCreatedAt());
        SmsCodeTransactionDto smsCodeTransactionDto = new SmsCodeTransactionDto(
                givenPhoneNumber, // phoneNumber
                beforeCode, // before
                afterCode // after
        );

        // When & Then
        assertThatThrownBy(() -> smsCode.rollback(smsCodeTransactionDto))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
