package com.homfo.user.usecase;

import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;

/**
 * 액세스 토큰을 리프레쉬합니다.
 * */
public interface TokenRefreshUsecase {
     /**
      * 액세스 토큰을 리프레쉬합니다.
      * */
     JwtDto refreshToken(long userId, TokenRefreshCommand command);
}
