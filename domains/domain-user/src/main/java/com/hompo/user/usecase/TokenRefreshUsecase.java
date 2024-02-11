package com.hompo.user.usecase;

import com.hompo.auth.command.TokenRefreshCommand;
import com.hompo.auth.dto.JwtDto;


public interface TokenRefreshUsecase {
     JwtDto execute(long userId, TokenRefreshCommand command);
}
