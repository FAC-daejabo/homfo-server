package com.hompo.usecase;

import com.hompo.auth.command.TokenRefreshCommand;
import com.hompo.auth.dto.JwtDto;
import com.hompo.auth.dto.JwtSecretDto;
import com.hompo.auth.infra.util.JwtUtil;
import com.hompo.user.service.UserRefreshTokenReadService;
import com.hompo.user.service.UserRefreshTokenWriteService;
import com.hompo.user.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * [refreshToken]이 만료되지 않았다면 [accessToken]을 새로 발급합니다. [refreshToken]은 새로 발급하지 않습니다.
 * */
@RequiredArgsConstructor
@Service
public class TokenRefreshUsecase {
    private final UserRefreshTokenReadService userRefreshTokenReadService;

    private final JwtSecretDto userAccessTokenInfo;

    private final JwtSecretDto userRefreshTokenInfo;

    public JwtDto execute(long userId, TokenRefreshCommand command) {
        String refreshToken = userRefreshTokenReadService.getVerifyToken(command.token(), userRefreshTokenInfo);
        String accessToken = JwtUtil.createToken(userId, userAccessTokenInfo);

        return new JwtDto(accessToken, refreshToken);
    }
}
