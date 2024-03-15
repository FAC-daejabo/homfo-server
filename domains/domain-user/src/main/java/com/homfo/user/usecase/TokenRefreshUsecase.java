package com.homfo.user.usecase;

import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;


public interface TokenRefreshUsecase {
     JwtDto refreshToken(long userId, TokenRefreshCommand command);
}
