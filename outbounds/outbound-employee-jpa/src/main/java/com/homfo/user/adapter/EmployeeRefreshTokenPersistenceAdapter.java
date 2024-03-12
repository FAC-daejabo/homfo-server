package com.homfo.user.adapter;

import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.infra.util.JwtUtil;
import com.homfo.auth.port.LoadJwtPort;
import com.homfo.auth.port.ManageJwtPort;
import com.homfo.user.entity.JpaEmployeeRefreshToken;
import com.homfo.user.infra.enums.EmployeeErrorCode;
import com.homfo.error.ResourceNotFoundException;
import com.homfo.user.repository.EmployeeRefreshTokenRepository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EmployeeRefreshTokenPersistenceAdapter implements LoadJwtPort, ManageJwtPort {
    private final EmployeeRefreshTokenRepository employeeRefreshTokenRepository;

    private final PasswordEncoder encoder;

    @Override
    public String save(long userId, @NonNull JwtSecretDto refreshTokenSecret) {
        String token = JwtUtil.createToken(userId, refreshTokenSecret);
        JpaEmployeeRefreshToken refreshToken = new JpaEmployeeRefreshToken(userId, encoder.encode(token));

        employeeRefreshTokenRepository.save(refreshToken);

        return token;
    }

    @Override
    public void deleteToken(long userId) {
        employeeRefreshTokenRepository.deleteById(userId);
    }

    @Override
    public String getVerifyToken(String token, JwtSecretDto jwtSecretDto) {
        Long userId = JwtUtil.getUserIdFromToken(token, jwtSecretDto);
        JpaEmployeeRefreshToken refreshToken = employeeRefreshTokenRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException(EmployeeErrorCode.NOT_EXIST_TOKEN));

        if(encoder.matches(token, refreshToken.getToken())) {
            return token;
        }

        throw new RuntimeException();
    }
}
