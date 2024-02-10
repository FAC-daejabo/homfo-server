package com.hompo.user.service;

import com.hompo.auth.dto.JwtSecretDto;
import com.hompo.auth.infra.util.JwtUtil;
import com.hompo.user.entity.MySqlRefreshToken;
import com.hompo.user.repository.RefreshTokenRepository;
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
            return refreshToken.getToken();
        }

        throw new RuntimeException();
    }
}
