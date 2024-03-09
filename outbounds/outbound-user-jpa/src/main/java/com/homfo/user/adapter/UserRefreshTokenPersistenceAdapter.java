package com.homfo.user.adapter;

import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.auth.port.LoadJwtPort;
import com.homfo.auth.port.ManageJwtPort;
import com.homfo.user.entity.JpaUserRefreshToken;
import com.homfo.error.ResourceNotFoundException;
import com.homfo.user.repository.UserRefreshTokenRepository;
import com.homfo.user.infra.enums.UserErrorCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserRefreshTokenPersistenceAdapter implements LoadJwtPort, ManageJwtPort {
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    private final PasswordEncoder encoder;

    @Override
    public String save(long userId, @NonNull JwtSecretDto refreshTokenSecret) {
        String token = JwtUtil.createToken(userId, refreshTokenSecret);
        JpaUserRefreshToken refreshToken = new JpaUserRefreshToken(userId, encoder.encode(token));

        userRefreshTokenRepository.save(refreshToken);

        return token;
    }

    @Override
    public void deleteToken(long userId) {
        userRefreshTokenRepository.deleteById(userId);
    }

    @Override
    public String getVerifyToken(String token, JwtSecretDto jwtSecretDto) {
        Long userId = JwtUtil.getUserIdFromToken(token, jwtSecretDto);
        JpaUserRefreshToken refreshToken = userRefreshTokenRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(UserErrorCode.NOT_EXIST_TOKEN));

        if(encoder.matches(token, refreshToken.getToken())) {
            return token;
        }

        throw new RuntimeException();
    }
}
