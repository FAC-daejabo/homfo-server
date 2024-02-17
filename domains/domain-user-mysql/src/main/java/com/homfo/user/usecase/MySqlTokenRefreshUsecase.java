package com.homfo.user.usecase;

import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.user.service.UserRefreshTokenReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MySqlTokenRefreshUsecase implements TokenRefreshUsecase {
    private final UserRefreshTokenReadService userRefreshTokenReadService;

    private final JwtSecretDto userAccessTokenInfo;

    private final JwtSecretDto userRefreshTokenInfo;

    public JwtDto execute(long userId, TokenRefreshCommand command) {
        String refreshToken = userRefreshTokenReadService.getVerifyToken(command.token(), userRefreshTokenInfo);
        String accessToken = JwtUtil.createToken(userId, userAccessTokenInfo);

        return new JwtDto(accessToken, refreshToken);
    }
}
