package com.hompo.user.usecase;

import com.hompo.auth.dto.JwtSecretDto;
import com.hompo.user.service.UserRefreshTokenWriteService;
import com.hompo.user.service.UserWriteService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MySqlDeleteAccountUsecase implements DeleteAccountUsecase{
    private final UserWriteService userWriteService;

    private final UserRefreshTokenWriteService userRefreshTokenWriteService;

    public MySqlDeleteAccountUsecase(
            UserWriteService userWriteService,
            UserRefreshTokenWriteService userRefreshTokenWriteService
    ) {
        this.userWriteService = userWriteService;
        this.userRefreshTokenWriteService = userRefreshTokenWriteService;
    }

    @Transactional
    public void execute(long userId) {
        userWriteService.deleteAccount(userId);
        userRefreshTokenWriteService.deleteByUserId(userId);
    }
}
