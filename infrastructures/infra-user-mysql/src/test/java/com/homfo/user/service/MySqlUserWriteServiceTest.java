package com.homfo.user.service;

import com.homfo.user.command.RegisterCommand;
import com.homfo.user.dto.UserDto;
import com.homfo.user.entity.MySqlUser;
import com.homfo.user.infra.enums.UserStatus;
import com.homfo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MySqlUserWriteServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private MySqlUserWriteService userWriteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(encoder.encode(anyString())).thenReturn(new BCryptPasswordEncoder().encode("Test@123"));
    }

    @Test
    @DisplayName("새 사용자 등록")
    void givenNewUser_whenRegister_thenSavesUserCorrectly() {
        // given
        RegisterCommand command = new RegisterCommand("testAccount", "Test@123!", "TestUser", "010-1234-5678", null, "Developer", LocalDate.of(1990, 1, 1), null);
        when(userRepository.findByAccountOrNicknameOrPhoneNumber(command.account(), command.nickname(), command.phoneNumber())).thenReturn(Optional.empty());

        // when
        UserDto result = userWriteService.register(command);

        // then
        assertThat(result.account()).isEqualTo(command.account());
        verify(userRepository, times(1)).save(any(MySqlUser.class));
    }

    @Test
    @DisplayName("등록된 사용자의 정보와 똑같은 정보로 새 사용자를 등록할 때, 적절한 예외를 던진다")
    void givenExistingUserInfo_whenRegister_thenThrowsException() {
        // given
        RegisterCommand command = new RegisterCommand("testAccount", "password123!", "TestUser", "010-1234-5678", null, "Developer", LocalDate.of(1990, 1, 1), null);
        MySqlUser user = MySqlUser.builder()
                .account(command.account())
                .password(encoder.encode(command.password()))
                .nickname(command.nickname())
                .phoneNumber(command.phoneNumber())
                .build();
        when(userRepository.findByAccountOrNicknameOrPhoneNumber(command.account(), command.nickname(), command.phoneNumber())).thenReturn(Optional.of(user));

        // when
        Executable result = () -> userWriteService.register(command);

        // then
        assertThrows(RuntimeException.class, result, "등록된 사용자 정보로 새 사용자를 등록하려고 할 때, 적절한 예외를 던져야 합니다.");
    }

    @Test
    @DisplayName("등록된 사용자의 정보와 똑같은 계정으로 새 사용자를 등록할 때, 적절한 예외를 던진다")
    void givenExistingAccount_whenRegister_thenThrowsException() {
        // given
        RegisterCommand command = new RegisterCommand("testAccount", "password123!", "TestUser", "010-1234-5678", null, "Developer", LocalDate.of(1990, 1, 1), null);
        MySqlUser user = MySqlUser.builder()
                .account(command.account())
                .password(encoder.encode(command.password()))
                .nickname("Another")
                .phoneNumber("999-9999-9999")
                .build();
        when(userRepository.findByAccountOrNicknameOrPhoneNumber(command.account(), command.nickname(), command.phoneNumber())).thenReturn(Optional.of(user));

        // when
        Executable result = () -> userWriteService.register(command);

        // then
        assertThrows(RuntimeException.class, result, "등록된 사용자 정보로 새 사용자를 등록하려고 할 때, 적절한 예외를 던져야 합니다.");
    }

    @Test
    @DisplayName("등록된 사용자의 정보와 똑같은 닉네임으로 새 사용자를 등록할 때, 적절한 예외를 던진다")
    void givenExistingNickname_whenRegister_thenThrowsException() {
        // given
        RegisterCommand command = new RegisterCommand("testAccount", "password123!", "TestUser", "010-1234-5678", null, "Developer", LocalDate.of(1990, 1, 1), null);
        MySqlUser user = MySqlUser.builder()
                .account("AnotherAccount")
                .password(encoder.encode(command.password()))
                .nickname(command.nickname())
                .phoneNumber("999-9999-9999")
                .build();
        when(userRepository.findByAccountOrNicknameOrPhoneNumber(command.account(), command.nickname(), command.phoneNumber())).thenReturn(Optional.of(user));

        // when
        Executable result = () -> userWriteService.register(command);

        // then
        assertThrows(RuntimeException.class, result, "등록된 사용자 정보로 새 사용자를 등록하려고 할 때, 적절한 예외를 던져야 합니다.");
    }

    @Test
    @DisplayName("등록된 사용자의 정보와 똑같은 전화번호로 새 사용자를 등록할 때, 적절한 예외를 던진다")
    void givenExistingPhoneNumber_whenRegister_thenThrowsException() {
        // given
        RegisterCommand command = new RegisterCommand("testAccount", "password123!", "TestUser", "010-1234-5678", null, "Developer", LocalDate.of(1990, 1, 1), null);
        MySqlUser user = MySqlUser.builder()
                .account("AnotherAccount")
                .password(encoder.encode(command.password()))
                .nickname("Another")
                .phoneNumber("010-1234-5678")
                .build();
        when(userRepository.findByAccountOrNicknameOrPhoneNumber(command.account(), command.nickname(), command.phoneNumber())).thenReturn(Optional.of(user));

        // when
        Executable result = () -> userWriteService.register(command);

        // then
        assertThrows(RuntimeException.class, result, "등록된 사용자 정보로 새 사용자를 등록하려고 할 때, 적절한 예외를 던져야 합니다.");
    }

    @Test
    @DisplayName("계정 삭제")
    void givenUserId_whenDeleteAccount_thenDeletesAccount() {
        // given
        long userId = 1L;
        MySqlUser user = MySqlUser.builder()
                .account("testAccount")
                .password(encoder.encode("Test@123"))
                .nickname("testNickname")
                .phoneNumber("999-9999-9999")
                .build();
        when(userRepository.findByIdAndStatusNot(userId, UserStatus.DELETED)).thenReturn(Optional.of(user));

        // when
        userWriteService.deleteAccount(userId);

        // then
        assertThat(user.getStatus()).isEqualTo(UserStatus.DELETED);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("존재하지 않는 계정을 삭제시도")
    void givenNotExistOrDeletedUserId_whenDeleteAccount_thenThrowsException() {
        // given
        long userId = 1;
        when(userRepository.findByIdAndStatusNot(userId, UserStatus.DELETED)).thenReturn(Optional.empty());

        // when
        Executable result = () -> userWriteService.deleteAccount(userId);

        // then
        assertThrows(RuntimeException.class, result, "존재하지 않는 계정을 삭제 시도하면 에러가 발생합니다.");
    }
}
