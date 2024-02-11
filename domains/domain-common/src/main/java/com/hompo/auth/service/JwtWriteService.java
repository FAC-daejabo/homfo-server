package com.hompo.auth.service;

import com.hompo.auth.command.TokenRefreshCommand;
import com.hompo.auth.dto.JwtSecretDto;
import lombok.NonNull;

/**
 * Refresh Token을 DB에 저장합니다.
 * */
public interface JwtWriteService {
    /**
     * Refresh Token을 DB에 저장합니다.
     *
     * 암호화되지 않은 원본 토큰 문자열을 반환합니다.
     * */
    String save(long userId, @NonNull JwtSecretDto jwtSecretDto);

    /**
     * Refresh Token을 새로 발급해 DB에 저장합니다.
     *
     * 암호화되지 않은 원본 토큰 문자열을 반환합니다.
     * */
    String refresh(long userId, @NonNull TokenRefreshCommand command, @NonNull JwtSecretDto jwtSecretDto);

    /**
     * Refresh Token을 삭제합니다.
     * */
    void deleteByUserId(long userId);
}
