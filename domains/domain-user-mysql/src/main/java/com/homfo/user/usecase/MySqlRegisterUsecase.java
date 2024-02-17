package com.homfo.user.usecase;

import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.user.command.RegisterCommand;
import com.homfo.user.dto.UserDto;
import com.homfo.user.service.UserMarketingAgreementWriteService;
import com.homfo.user.service.UserRefreshTokenWriteService;
import com.homfo.user.service.UserWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MySqlRegisterUsecase implements RegisterUsecase {
    private final UserWriteService userWriteService;

    private final UserRefreshTokenWriteService userRefreshTokenWriteService;

    private final UserMarketingAgreementWriteService userMarketingAgreementWriteService;

    private final JwtSecretDto userAccessTokenInfo;

    private final JwtSecretDto userRefreshTokenInfo;

    /**
     * 사용자 정보를 등록합니다.
     *
     * 하나라도 실패하면 회원가입이 실패해야 해 한 트랜잭션으로 묶습니다.
     * */
    @Transactional
    public JwtDto execute(RegisterCommand command) {
        UserDto userDto = userWriteService.register(command);
        String accessToken = JwtUtil.createToken(userDto.id(), userAccessTokenInfo);
        String refreshToken = userRefreshTokenWriteService.save(userDto.id(), userRefreshTokenInfo);

        userMarketingAgreementWriteService.save(command, userDto);

        return new JwtDto(accessToken, refreshToken);
    }
}
