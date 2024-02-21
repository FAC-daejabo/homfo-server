package com.homfo.user.service;

import com.homfo.user.command.RegisterCommand;
import com.homfo.user.dto.UserDto;
import lombok.NonNull;

/**
 * 사용자 정보를 DB에 저장합니다.
 * */
public interface UserWriteService {
    /**
     * 회원가입합니다.
     * */
    UserDto register(@NonNull RegisterCommand command);

    /**
     * 계정 정보를 삭제합니다.
     * */
    void deleteAccount(long userId);
}
