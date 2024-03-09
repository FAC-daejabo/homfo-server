package com.homfo.user.service;

import com.homfo.user.command.SignInCommand;
import com.homfo.user.dto.UserDto;
import com.homfo.user.entity.MySqlUser;
import com.homfo.user.infra.util.ValidationUtil;
import com.homfo.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class MySqlUserReadServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private MySqlUserReadService userReadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("ID로 사용자를 찾을 수 있어야 한다")
    void givenUserId_whenFindById_thenReturnsUserDto() {
        // Given
        long userId = 1L;
        MySqlUser mockUser = mock(MySqlUser.class);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(mockUser.getId()).thenReturn(userId);
        when(mockUser.getAccount()).thenReturn("testAccount");

        // When
        UserDto result = userReadService.findById(userId);

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
        MySqlUser mockUser = mock(MySqlUser.class);
        when(userRepository.findByAccount(command.account())).thenReturn(Optional.of(mockUser));
        when(encoder.matches(command.password(), mockUser.getPassword())).thenReturn(true);
        when(mockUser.getId()).thenReturn(1L);
        when(mockUser.getAccount()).thenReturn(command.account());

        // When
        UserDto result = userReadService.signIn(command);

        // Then
        assertThat(result.account()).isEqualTo(command.account());
        verify(userRepository, times(1)).findByAccount(command.account());
        verify(mockUser, times(1)).signIn(encoder, command.password());
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 로그인 시도 시 예외를 던져야 한다")
    void givenInvalidSignInCommand_whenSignIn_thenThrowsException() {
        // Given
        SignInCommand command = new SignInCommand("nonExistentAccount", "password123");
        when(userRepository.findByAccount(command.account())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RuntimeException.class, () -> userReadService.signIn(command));
    }
}
