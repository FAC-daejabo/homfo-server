package com.homfo.user.adapter;

import com.homfo.sms.command.ValidateSmsCodeCommand;
import com.homfo.sms.dto.SmsCodeDto;
import com.homfo.sms.dto.SmsCodeTransactionDto;
import com.homfo.sms.infra.enums.SmsCodeStatus;
import com.homfo.user.entity.JpaUserSmsCode;
import com.homfo.user.repository.UserSmsCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserSmsCodePersistenceAdapterTest {
    @Mock
    private UserSmsCodeRepository repository;

    @InjectMocks
    private UserSmsCodePersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("최초로 새로운 SMS 코드 생성")
    void createSmsCode_NewCode_CreatesSuccessfully() {
        // Given
        String phoneNumber = "010-1234-5678";
        when(repository.findById(phoneNumber)).thenReturn(Optional.empty());

        // When
        SmsCodeTransactionDto result = adapter.createSmsCode(phoneNumber);

        // Then
        verify(repository, times(1)).save(any(JpaUserSmsCode.class));
        assertThat(result.phoneNumber()).isEqualTo(phoneNumber);
        assertThat(result.before().phoneNumber()).isEqualTo(phoneNumber);
        assertThat(result.before().code()).isNull();
        assertThat(result.before().firstCreatedAt()).isNull();
        assertThat(result.before().createdAt()).isNull();
        assertThat(result.after().phoneNumber()).isEqualTo(phoneNumber);
        assertThat(result.after().firstCreatedAt()).isEqualTo(result.after().createdAt());
    }

    @Test
    @DisplayName("SMS 코드 검증 성공")
    void verifySmsCode_ValidCode_ReturnsTrue() {
        // Given
        String phoneNumber = "010-1234-5678";
        String code = "123456";
        ValidateSmsCodeCommand command = new ValidateSmsCodeCommand(phoneNumber, code);
        JpaUserSmsCode jpaUserSmsCode = mock(JpaUserSmsCode.class);
        when(repository.findByPhoneNumberAndCode(phoneNumber, code)).thenReturn(Optional.of(jpaUserSmsCode));
        when(jpaUserSmsCode.verifyCode(command)).thenReturn(true);

        // When
        boolean result = adapter.verifySmsCode(command);

        // Then
        assertThat(result).isTrue();
        verify(repository, times(1)).save(jpaUserSmsCode);
    }

    @Test
    @DisplayName("성공한 SMS 코드 존재 확인")
    void existSuccessSmsCode_Exists_ReturnsTrue() {
        // Given
        String phoneNumber = "010-1234-5678";
        JpaUserSmsCode jpaUserSmsCode = mock(JpaUserSmsCode.class);
        when(repository.findByPhoneNumberAndStatus(phoneNumber, SmsCodeStatus.SUCCESS)).thenReturn(Optional.of(jpaUserSmsCode));
        when(jpaUserSmsCode.isExpired()).thenReturn(false);

        // When
        boolean result = adapter.existSuccessSmsCode(phoneNumber);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("SMS 코드 롤백 성공")
    void rollbackSmsCode_ValidTransaction_RollbacksSuccessfully() {
        // Given
        String phoneNumber = "010-1234-5678";
        SmsCodeTransactionDto smsCodeTransactionDto = mock(SmsCodeTransactionDto.class);
        JpaUserSmsCode jpaUserSmsCode = mock(JpaUserSmsCode.class);

        when(jpaUserSmsCode.getPhoneNumber()).thenReturn(phoneNumber);
        when(smsCodeTransactionDto.phoneNumber()).thenReturn(phoneNumber);
        when(repository.findById(phoneNumber)).thenReturn(Optional.of(jpaUserSmsCode));

        // When
        SmsCodeDto result = adapter.rollbackSmsCode(smsCodeTransactionDto);

        // Then
        assertThat(result.phoneNumber()).isEqualTo(phoneNumber);
        verify(jpaUserSmsCode, times(1)).rollback(smsCodeTransactionDto);
        verify(repository, times(1)).save(jpaUserSmsCode);
    }

    @Test
    @DisplayName("최초로 새로운 SMS 코드 생성 이후 SMS 코드 롤백 성공")
    void rollbackSmsCode_ValidTransaction_RollbacksSuccessfully_AfterInitialCreatedSms() {
        // Given
        String phoneNumber = "010-1234-5678";
        when(repository.findById(phoneNumber)).thenReturn(Optional.empty());
        SmsCodeTransactionDto smsCodeTransactionDto = adapter.createSmsCode(phoneNumber);
        JpaUserSmsCode jpaUserSmsCode = mock(JpaUserSmsCode.class);

        when(jpaUserSmsCode.getPhoneNumber()).thenReturn(phoneNumber);
        when(repository.findById(phoneNumber)).thenReturn(Optional.of(jpaUserSmsCode));

        // When
        SmsCodeDto result = adapter.rollbackSmsCode(smsCodeTransactionDto);

        // Then
        assertThat(result.phoneNumber()).isEqualTo(phoneNumber);
        assertThat(result.code()).isNull();
        assertThat(result.firstCreatedAt()).isNull();
        assertThat(result.createdAt()).isNull();
        assertThat(result.status()).isNull();
        verify(jpaUserSmsCode, times(1)).rollback(smsCodeTransactionDto);
        verify(repository, times(1)).save(jpaUserSmsCode);
    }
}
