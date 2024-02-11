package com.hompo.user.usecase;

import com.hompo.auth.command.TokenRefreshCommand;
import com.hompo.auth.dto.JwtDto;
import com.hompo.auth.dto.JwtSecretDto;
import com.hompo.auth.infra.util.JwtUtil;
import com.hompo.user.service.UserRefreshTokenReadService;
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
