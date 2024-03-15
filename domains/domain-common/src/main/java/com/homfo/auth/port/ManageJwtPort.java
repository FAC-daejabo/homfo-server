package com.homfo.auth.port;

import com.homfo.auth.dto.JwtSecretDto;
import lombok.NonNull;

/**
 * Refresh Token을 DB에 저장합니다.
 */
public interface ManageJwtPort {
    /**
     * Refresh Token을 DB에 저장합니다.
     * <p>
     * 암호화되지 않은 원본 토큰 문자열을 반환합니다.
     */
    String save(long userId, @NonNull JwtSecretDto jwtSecretDto);

    /**
     * Refresh Token을 삭제합니다.
     */
    void deleteToken(long userId);
}
