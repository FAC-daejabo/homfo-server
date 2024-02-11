package com.hompo.user.usecase;

import com.hompo.auth.dto.JwtDto;
import com.hompo.auth.dto.JwtSecretDto;
import com.hompo.auth.infra.util.JwtUtil;
import com.hompo.user.command.SignInCommand;
import com.hompo.user.dto.UserDto;
import com.hompo.user.service.UserRefreshTokenWriteService;
import com.hompo.user.service.UserWriteService;
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
