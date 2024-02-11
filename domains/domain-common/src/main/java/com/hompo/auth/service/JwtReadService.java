package com.hompo.auth.service;

import com.hompo.auth.dto.JwtSecretDto;

/**
 * Refresh Token을 DB로부터 읽어옵니다.
 * */
public interface JwtReadService {
    /**
     * 인증되었고 만료되지 않은 Refresh Token인지 확인합니다.
     *
     * 올바른 [token]이라면 암호화되지 않은 원본 [token]을 return 합니다.
     * */
    String getVerifyToken(String token, JwtSecretDto jwtSecretDto);
}
