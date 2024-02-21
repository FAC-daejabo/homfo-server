package com.homfo.user.usecase;

import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.user.command.SignInCommand;
import com.homfo.user.dto.UserDto;
import com.homfo.user.service.UserReadService;
import com.homfo.user.service.UserRefreshTokenWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MySqlSignInUsecase implements SignInUsecase {
    private final UserReadService userReadService;

    private final UserRefreshTokenWriteService userRefreshTokenWriteService;

    private final JwtSecretDto userAccessTokenInfo;

    private final JwtSecretDto userRefreshTokenInfo;


    /**
     * 로그인합니다. Jwt 정보를 반환합니다.
     *
     * 리프레쉬 토큰 재발급만 DB에서 제대로 처리되면 되기에, 트랜잭션으로 묶지 않습니다.
     * */
    public JwtDto execute(SignInCommand command) {
        UserDto userDto = userReadService.signIn(command);
        String accessToken = JwtUtil.createToken(userDto.id(), userAccessTokenInfo);
        String refreshToken = userRefreshTokenWriteService.save(userDto.id(), userRefreshTokenInfo);

        return new JwtDto(accessToken, refreshToken);
    }
}
