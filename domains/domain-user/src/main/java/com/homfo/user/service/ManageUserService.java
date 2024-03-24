package com.homfo.user.service;

import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.auth.port.LoadJwtPort;
import com.homfo.auth.port.ManageJwtPort;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.command.SignInCommand;
import com.homfo.user.dto.UserDto;
import com.homfo.user.dto.UserMarketingAgreementDto;
import com.homfo.user.port.LoadUserMarketingAgreementPort;
import com.homfo.user.port.LoadUserPort;
import com.homfo.user.port.ManageUserPort;
import com.homfo.user.port.ManageUserMarketingAgreementPort;
import com.homfo.user.usecase.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManageUserService implements GetUserInfoUsecase, SignInUsecase, SignOutUsecase, RegisterUsecase, DeleteAccountUsecase, TokenRefreshUsecase {
    private final LoadUserPort loadUserPort;

    private final LoadJwtPort loadJwtPort;

    private final LoadUserMarketingAgreementPort loadUserMarketingAgreementPort;

    private final ManageUserPort manageUserAccountPort;

    private final ManageJwtPort manageJwtPort;

    private final ManageUserMarketingAgreementPort manageUserMarketingAgreementPort;

    private final JwtSecretDto accessTokenInfo;

    private final JwtSecretDto refreshTokenInfo;

    @Autowired
    public ManageUserService(
            LoadUserPort loadUserPort,
            @Qualifier("userRefreshTokenPersistenceAdapter") LoadJwtPort loadJwtPort,
            LoadUserMarketingAgreementPort loadUserMarketingAgreementPort,
            ManageUserPort manageUserAccountPort,
            @Qualifier("userRefreshTokenPersistenceAdapter") ManageJwtPort manageJwtPort,
            ManageUserMarketingAgreementPort manageUserMarketingAgreementPort,
            JwtSecretDto accessTokenInfo,
            JwtSecretDto refreshTokenInfo
    ) {
        this.loadUserPort = loadUserPort;
        this.loadJwtPort = loadJwtPort;
        this.loadUserMarketingAgreementPort = loadUserMarketingAgreementPort;
        this.manageUserAccountPort = manageUserAccountPort;
        this.manageJwtPort = manageJwtPort;
        this.manageUserMarketingAgreementPort = manageUserMarketingAgreementPort;
        this.accessTokenInfo = accessTokenInfo;
        this.refreshTokenInfo = refreshTokenInfo;
    }

    @Override
    public UserMarketingAgreementDto getUserInfo(long userId) {
        UserDto userDto = loadUserPort.loadUser(userId);
        return loadUserMarketingAgreementPort.loadMarketingAgreement(userDto);
    }

    @Override
    @Transactional
    public void deleteAccount(long userId) {
        manageUserAccountPort.deleteAccount(userId);
        manageJwtPort.deleteToken(userId);
    }

    @Override
    @Transactional
    public JwtDto register(RegisterCommand command) {
        UserDto userDto = manageUserAccountPort.register(command);
        String accessToken = JwtUtil.createToken(userDto.id(), accessTokenInfo);
        String refreshToken = manageJwtPort.save(userDto.id(), refreshTokenInfo);

        manageUserMarketingAgreementPort.save(command, userDto);

        return new JwtDto(accessToken, refreshToken);
    }

    @Override
    public JwtDto signIn(SignInCommand command) {
        UserDto userDto = loadUserPort.signIn(command);
        String accessToken = JwtUtil.createToken(userDto.id(), accessTokenInfo);
        String refreshToken = manageJwtPort.save(userDto.id(), refreshTokenInfo);

        return new JwtDto(accessToken, refreshToken);
    }

    @Override
    public JwtDto refreshToken(long userId, TokenRefreshCommand command) {
        String refreshToken = loadJwtPort.getVerifyToken(command.token(), refreshTokenInfo);
        String accessToken = JwtUtil.createToken(userId, accessTokenInfo);

        return new JwtDto(accessToken, refreshToken);
    }

    @Override
    public void signOut(long userId) {
        manageJwtPort.deleteToken(userId);
    }
}
