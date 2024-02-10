package com.hompo.usecase;

import com.hompo.auth.dto.JwtDto;
import com.hompo.auth.dto.JwtInfoDto;
import com.hompo.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import com.hompo.user.dto.RegisterCommand;
import com.hompo.user.service.UserWriteService;

import java.util.function.Function;

@Service
public class RegisterUsecase {
    private final UserWriteService userWriteService;

    private final JwtService jwtService;

    private final JwtInfoDto accessTokenInfo;

    private final JwtInfoDto refreshTokenInfo;

    public RegisterUsecase(
            UserWriteService userWriteService,
            JwtService jwtService,
            @Qualifier("accessTokenInfo") JwtInfoDto accessTokenInfo,
            @Qualifier("refreshTokenInfo") JwtInfoDto refreshTokenInfo
    ) {
        this.userWriteService = userWriteService;
        this.jwtService = jwtService;
        this.accessTokenInfo = accessTokenInfo;
        this.refreshTokenInfo = refreshTokenInfo;
    }

    public JwtDto execute(RegisterCommand command) {
        Function<Long, String> getAccessToken = (Long userId) -> jwtService.createToken(String.valueOf(userId), accessTokenInfo);
        Function<Long, String> getRefreshToken = (Long userId) -> jwtService.createToken(String.valueOf(userId), refreshTokenInfo);
        return userWriteService.register(command, getAccessToken, getRefreshToken);
    }
}
