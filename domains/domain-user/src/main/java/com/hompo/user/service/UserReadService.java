package com.hompo.user.service;

import com.hompo.user.command.SignInCommand;
import com.hompo.user.dto.UserDto;
import lombok.NonNull;

/**
 * 사용자 정보를 읽어옵니다.
 * */
public interface UserReadService {
    /**
     * 특정 사용자 정보를 읽어옵니다.
     * */
    UserDto findById(long userId);

    /**
     * 특정 사용자에 대하여 로그인을 시도합니다. 성공하면 UserDto를 반환합니다.
     * */
    UserDto signIn(@NonNull SignInCommand command);
}
