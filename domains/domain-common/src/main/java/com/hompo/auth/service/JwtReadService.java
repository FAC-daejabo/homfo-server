package com.hompo.auth.service;

import com.hompo.auth.dto.JwtSecretDto;

public interface JwtReadService {
    String getVerifyToken(String token, JwtSecretDto jwtSecretDto);
}
