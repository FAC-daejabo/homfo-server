package com.homfo.user.usecase;

import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;
import com.homfo.error.ResourceNotFoundException;

/**
 * 액세스 토큰을 리프레쉬합니다.
 * */
public interface TokenRefreshUsecase {
     /**
      * 액세스 토큰을 리프레쉬합니다.
      *
      * @throws ResourceNotFoundException 토큰 정보가 없다면
      * */
     JwtDto refreshToken(long userId, TokenRefreshCommand command);
}
