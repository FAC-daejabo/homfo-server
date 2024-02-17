package com.homfo.user.usecase;

import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.user.service.UserRefreshTokenWriteService;
import com.homfo.user.service.UserWriteService;
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

    /**
     * 사용자 정보를 삭제합니다.
     *
     * 하나라도 실패하면 삭제가 실패해야 해 한 트랜잭션으로 묶습니다.
     * */
    @Transactional
    public void execute(long userId) {
        userWriteService.deleteAccount(userId);
        userRefreshTokenWriteService.deleteByUserId(userId);
    }
}
