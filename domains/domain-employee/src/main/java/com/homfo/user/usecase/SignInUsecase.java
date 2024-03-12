package com.homfo.user.usecase;

import com.homfo.auth.dto.JwtDto;
import com.homfo.user.command.SignInCommand;
import com.homfo.error.ResourceNotFoundException;


/**
 * 직원이 로그인 합니다.
 * */
public interface SignInUsecase {
    /**
     * 특정 직원이 로그인 합니다. Jwt 정보를 반환합니다.
     *
     * @throws ResourceNotFoundException 직원 정보가 없다면
     * */
    JwtDto signIn(SignInCommand command);
}
