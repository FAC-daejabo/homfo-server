package com.homfo.user.service;

import com.homfo.auth.command.TokenRefreshCommand;
import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.user.entity.MySqlRefreshToken;
import com.homfo.user.repository.RefreshTokenRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MySqlUserRefreshTokenWriteService implements UserRefreshTokenWriteService {
    private final RefreshTokenRepository refreshTokenRepository;

    private final PasswordEncoder encoder;

    @Override
    public String save(long userId, @NonNull JwtSecretDto jwtSecretDto) {
        String token = JwtUtil.createToken(userId, jwtSecretDto);
        MySqlRefreshToken refreshToken = new MySqlRefreshToken(userId, encoder.encode(token));

        refreshTokenRepository.save(refreshToken);

        return token;
    }

    @Override
    public void deleteByUserId(long userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
