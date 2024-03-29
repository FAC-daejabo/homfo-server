package com.homfo.user.adapter;

import com.homfo.error.ResourceAlreadyExistException;
import com.homfo.error.ResourceNotFoundException;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.command.SignInCommand;
import com.homfo.user.dto.UserDto;
import com.homfo.user.entity.JpaUser;
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

class UserPersistenceAdapterTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private UserPersistenceAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        when(encoder.encode(anyString())).thenReturn(new BCryptPasswordEncoder().encode("Test@123"));
    }

    @Test
    @DisplayName("ID로 사용자를 찾을 수 있어야 한다")
    void givenUserId_whenFindById_thenReturnsUserDto() {
        // Given
        long userId = 1L;
        JpaUser mockUser = mock(JpaUser.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockUser.getId()).thenReturn(userId);
        when(mockUser.getAccount()).thenReturn("testAccount");

        // When
        UserDto result = adapter.loadUser(userId);

        // Then
        assertThat(result.id()).isEqualTo(userId);
        assertThat(result.account()).isEqualTo("testAccount");
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("로그인 시 유효한 사용자 정보를 반환해야 한다")
    void givenValidSignInCommand_whenSignIn_thenReturnsUserDto() {
        // Given
        SignInCommand command = new SignInCommand("testAccount", "ValidPass123!");
        JpaUser mockUser = mock(JpaUser.class);
        when(userRepository.findByAccount(command.account())).thenReturn(Optional.of(mockUser));
        when(encoder.matches(command.password(), mockUser.getPassword())).thenReturn(true);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getAccount()).thenReturn(command.account());

        // When
        UserDto result = adapter.signIn(command);

        // Then
        assertThat(result.account()).isEqualTo(command.account());
        verify(userRepository, times(1)).findByAccount(command.account());
        verify(mockUser, times(1)).signIn(encoder, command.password());
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 로그인 시도 시 예외를 던져야 한다")
    void givenInvalidSignInCommand_whenSignIn_thenThrowsException() {
        // Given
        SignInCommand command = new SignInCommand("nonExist", "Password123!");
        when(userRepository.findByAccount(command.account())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> adapter.signIn(command));
    }

    @Test
    @DisplayName("새 사용자 등록")
    void givenNewUser_whenRegister_thenSavesUserCorrectly() {
        // given
        RegisterCommand command = new RegisterCommand("testAccount", "Test@123!", "TestUser", "010-1234-5678", null, "Developer", LocalDate.of(1990, 1, 1), null);
        when(userRepository.findByAccountOrNicknameOrPhoneNumber(command.account(), command.nickname(), command.phoneNumber())).thenReturn(Optional.empty());

        // when
        UserDto result = adapter.register(command);

        // then
        assertThat(result.account()).isEqualTo(command.account());
        verify(userRepository, times(1)).save(any(JpaUser.class));
    }

    @Test
    @DisplayName("등록된 사용자의 정보와 똑같은 정보로 새 사용자를 등록할 때, 적절한 예외를 던진다")
    void givenExistingUserInfo_whenRegister_thenThrowsException() {
        // given
        RegisterCommand command = new RegisterCommand("testAccount", "Password123!", "TestUser", "010-1234-5678", null, "Developer", LocalDate.of(1990, 1, 1), null);
        JpaUser user = JpaUser.builder()
                .account(command.account())
                .password(encoder.encode(command.password()))
                .nickname(command.nickname())
                .phoneNumber(command.phoneNumber())
                .build();
        when(userRepository.findByAccountOrNicknameOrPhoneNumber(command.account(), command.nickname(), command.phoneNumber())).thenReturn(Optional.of(user));

        // when
        Executable result = () -> adapter.register(command);

        // then
        assertThrows(ResourceAlreadyExistException.class, result, "등록된 사용자 정보로 새 사용자를 등록하려고 할 때, 적절한 예외를 던져야 합니다.");
    }

    @Test
    @DisplayName("등록된 사용자의 정보와 똑같은 계정으로 새 사용자를 등록할 때, 적절한 예외를 던진다")
    void givenExistingAccount_whenRegister_thenThrowsException() {
        // given
        RegisterCommand command = new RegisterCommand("testAccount", "Password123!", "TestUser", "010-1234-5678", null, "Developer", LocalDate.of(1990, 1, 1), null);
        JpaUser user = JpaUser.builder()
                .account(command.account())
                .password(encoder.encode(command.password()))
                .nickname("Another")
                .phoneNumber("999-9999-9999")
                .build();
        when(userRepository.findByAccountOrNicknameOrPhoneNumber(command.account(), command.nickname(), command.phoneNumber())).thenReturn(Optional.of(user));

        // when
        Executable result = () -> adapter.register(command);

        // then
        assertThrows(ResourceAlreadyExistException.class, result, "등록된 사용자 정보로 새 사용자를 등록하려고 할 때, 적절한 예외를 던져야 합니다.");
    }

    @Test
    @DisplayName("등록된 사용자의 정보와 똑같은 닉네임으로 새 사용자를 등록할 때, 적절한 예외를 던진다")
    void givenExistingNickname_whenRegister_thenThrowsException() {
        // given
        RegisterCommand command = new RegisterCommand("testAccount", "Password123!", "TestUser", "010-1234-5678", null, "Developer", LocalDate.of(1990, 1, 1), null);
        JpaUser user = JpaUser.builder()
                .account("AnotherAccount")
                .password(encoder.encode(command.password()))
                .nickname(command.nickname())
                .phoneNumber("999-9999-9999")
                .build();
        when(userRepository.findByAccountOrNicknameOrPhoneNumber(command.account(), command.nickname(), command.phoneNumber())).thenReturn(Optional.of(user));

        // when
        Executable result = () -> adapter.register(command);

        // then
        assertThrows(ResourceAlreadyExistException.class, result, "등록된 사용자 정보로 새 사용자를 등록하려고 할 때, 적절한 예외를 던져야 합니다.");
    }

    @Test
    @DisplayName("등록된 사용자의 정보와 똑같은 전화번호로 새 사용자를 등록할 때, 적절한 예외를 던진다")
    void givenExistingPhoneNumber_whenRegister_thenThrowsException() {
        // given
        RegisterCommand command = new RegisterCommand("testAccount", "Password123!", "TestUser", "010-1234-5678", null, "Developer", LocalDate.of(1990, 1, 1), null);
        JpaUser user = JpaUser.builder()
                .account("AnotherAccount")
                .password(encoder.encode(command.password()))
                .nickname("Another")
                .phoneNumber("010-1234-5678")
                .build();
        when(userRepository.findByAccountOrNicknameOrPhoneNumber(command.account(), command.nickname(), command.phoneNumber())).thenReturn(Optional.of(user));

        // when
        Executable result = () -> adapter.register(command);

        // then
        assertThrows(ResourceAlreadyExistException.class, result, "등록된 사용자 정보로 새 사용자를 등록하려고 할 때, 적절한 예외를 던져야 합니다.");
    }

    @Test
    @DisplayName("계정 삭제")
    void givenUserId_whenDeleteAccount_thenDeletesAccount() {
        // given
        long userId = 1L;
        JpaUser user = JpaUser.builder()
                .account("testAccount")
                .password(encoder.encode("Test@123"))
                .nickname("testNickname")
                .phoneNumber("999-9999-9999")
                .build();
        when(userRepository.findByIdAndStatusNot(userId, UserStatus.DELETED)).thenReturn(Optional.of(user));

        // when
        adapter.deleteAccount(userId);

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
        Executable result = () -> adapter.deleteAccount(userId);

        // then
        assertThrows(ResourceNotFoundException.class, result, "존재하지 않는 계정을 삭제 시도하면 에러가 발생합니다.");
    }
}
