package com.hompo.usecase;

import com.hompo.auth.command.TokenRefreshCommand;
import com.hompo.auth.dto.JwtDto;
import com.hompo.auth.dto.JwtSecretDto;
import com.hompo.auth.infra.util.JwtUtil;
import com.hompo.user.command.RegisterCommand;
import com.hompo.user.dto.UserDto;
import com.hompo.user.service.UserRefreshTokenWriteService;
import com.hompo.user.service.UserWriteService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TokenRefreshUsecase {
    private final UserWriteService userWriteService;

    private final UserRefreshTokenWriteService userRefreshTokenWriteService;

    private final JwtSecretDto accessTokenInfo;

    private final JwtSecretDto refreshTokenInfo;

    public TokenRefreshUsecase(
            UserWriteService userWriteService,
            UserRefreshTokenWriteService userRefreshTokenWriteService,
            @Qualifier("userAccessTokenInfo") JwtSecretDto accessTokenInfo,
            @Qualifier("userRefreshTokenInfo") JwtSecretDto refreshTokenInfo
    ) {
        this.userWriteService = userWriteService;
        this.userRefreshTokenWriteService = userRefreshTokenWriteService;
        this.accessTokenInfo = accessTokenInfo;
        this.refreshTokenInfo = refreshTokenInfo;
    }

    public JwtDto execute(long userId, TokenRefreshCommand command) {
        String accessToken = JwtUtil.createToken(userId, accessTokenInfo);
        String refreshToken = userRefreshTokenWriteService.refresh(userId, command, refreshTokenInfo);

        return new JwtDto(accessToken, refreshToken);
    }
}
