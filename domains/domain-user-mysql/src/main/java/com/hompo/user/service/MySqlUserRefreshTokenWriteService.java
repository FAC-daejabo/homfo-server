package com.hompo.user.service;

import com.hompo.auth.command.TokenRefreshCommand;
import com.hompo.auth.dto.JwtSecretDto;
import com.hompo.auth.infra.util.JwtUtil;
import com.hompo.user.entity.MySqlRefreshToken;
import com.hompo.user.repository.RefreshTokenRepository;
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
    public String refresh(long userId, @NonNull TokenRefreshCommand command, @NonNull JwtSecretDto jwtSecretDto) {
       if(!JwtUtil.verifyToken(command.token(), jwtSecretDto)) {
           throw new RuntimeException();
       }

       return save(userId, jwtSecretDto);
    }

    @Override
    public void deleteByUserId(long userId) {
        refreshTokenRepository.deleteById(userId);
    }
}
