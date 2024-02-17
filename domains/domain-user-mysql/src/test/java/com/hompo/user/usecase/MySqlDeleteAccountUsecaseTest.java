package com.hompo.user.usecase;

import com.hompo.user.service.UserRefreshTokenWriteService;
import com.hompo.user.service.UserWriteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class MySqlDeleteAccountUsecaseTest {

    @Mock
    private UserWriteService userWriteService;

    @Mock
    private UserRefreshTokenWriteService userRefreshTokenWriteService;

    @InjectMocks
    private MySqlDeleteAccountUsecase deleteAccountUsecase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("계정 삭제 시 UserWriteService와 UserRefreshTokenWriteService가 호출되어야 한다")
    void givenUserId_whenExecute_thenShouldCallServices() {
        // Given
        long userId = 1L;

        // When
        deleteAccountUsecase.execute(userId);

        // Then
        verify(userWriteService, times(1)).deleteAccount(userId);
        verify(userRefreshTokenWriteService, times(1)).deleteByUserId(userId);
    }
}
