package com.homfo.user.usecase;

import com.homfo.auth.dto.JwtDto;
import com.homfo.user.command.SignInCommand;

/**
 * 사용자가 로그인 합니다.
 * */
public interface SignInUsecase {
    /**
     * 특정 사용자가 로그인 합니다. Jwt 정보를 반환합니다.
     * */
    JwtDto execute(SignInCommand command);
}
