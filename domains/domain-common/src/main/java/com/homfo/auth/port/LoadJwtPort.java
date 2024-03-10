package com.homfo.auth.port;

import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.error.ResourceNotFoundException;

/**
 * Refresh Token을 DB로부터 읽어옵니다.
 * */
public interface LoadJwtPort {
    /**
     * 인증되었고 만료되지 않은 Refresh Token인지 확인합니다.
     *
     * @return token 올바른 토큰이라면 암호화되지 않은 원본 토큰을 반환
     * @throws ResourceNotFoundException 토큰 정보가 없다면
     * */
    String getVerifyToken(String token, JwtSecretDto jwtSecretDto);
}
