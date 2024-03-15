package com.homfo.user.port;

import com.homfo.error.ResourceNotFoundException;
import com.homfo.user.command.SignInCommand;
import com.homfo.user.dto.UserDto;
import lombok.NonNull;

public interface LoadUserPort {
    /**
     * 특정 사용자 정보를 읽어옵니다.
     *
     * @throws ResourceNotFoundException 사용자 정보가 없다면
     * */
    UserDto loadUser(long userId);

    /**
     * 특정 사용자에 대하여 로그인을 시도합니다.
     *
     * @throws ResourceNotFoundException 사용자 정보가 없다면
     * */
    UserDto signIn(@NonNull SignInCommand command);
}
