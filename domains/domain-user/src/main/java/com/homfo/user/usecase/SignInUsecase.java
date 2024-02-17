package com.homfo.user.usecase;

import com.homfo.auth.dto.JwtDto;
import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.user.command.SignInCommand;
import com.homfo.user.dto.UserDto;
import com.homfo.user.service.UserRefreshTokenWriteService;
import com.homfo.user.service.UserWriteService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * 사용자가 로그인 합니다.
 * */
public interface SignInUsecase {
    /**
     * 특정 사용자가 로그인 합니다. Jwt 정보를 반환합니다.
     * */
    JwtDto execute(SignInCommand command);
}
