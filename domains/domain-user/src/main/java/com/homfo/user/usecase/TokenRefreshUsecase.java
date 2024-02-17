package com.homfo.user.usecase;

import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;


public interface TokenRefreshUsecase {
     JwtDto execute(long userId, TokenRefreshCommand command);
}
