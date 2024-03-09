package com.homfo.user.service;

import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.user.entity.MySqlRefreshToken;
import com.homfo.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MySqlUserRefreshTokenReadService implements UserRefreshTokenReadService {
    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder encoder;

    @Override
    public String getVerifyToken(String token, JwtSecretDto jwtSecretDto) {
        Long userId = JwtUtil.getUserIdFromToken(token, jwtSecretDto);
        MySqlRefreshToken refreshToken = refreshTokenRepository.findById(userId).orElseThrow(RuntimeException::new);

        if(encoder.matches(token, refreshToken.getToken())) {
            return token;
        }

        throw new RuntimeException();
    }
}
