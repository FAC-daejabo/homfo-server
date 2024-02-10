package com.hompo.user.service;

import com.hompo.user.command.RegisterCommand;
import com.hompo.user.dto.UserDto;
import lombok.NonNull;
import com.hompo.user.command.SignInCommand;

import java.util.function.Function;

public interface UserWriteService {
    /**
     * 회원가입합니다.
     * */
    UserDto register(@NonNull RegisterCommand command);

    /**
     * 로그인합니다.
     * */
    UserDto signIn(@NonNull SignInCommand command);


    /**
     * 계정 정보를 삭제합니다.
     * */
    void deleteAccount(long userId);
}
