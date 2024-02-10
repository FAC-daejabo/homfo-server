package com.hompo.auth.service;

import com.hompo.auth.command.TokenRefreshCommand;
import com.hompo.auth.dto.JwtSecretDto;
import lombok.NonNull;

public interface JwtWriteService {
    String save(@NonNull long userId, @NonNull JwtSecretDto jwtSecretDto);

    String refresh(@NonNull long userId, @NonNull TokenRefreshCommand command, @NonNull JwtSecretDto jwtSecretDto);

    void deleteByUserId(long userId);
}
