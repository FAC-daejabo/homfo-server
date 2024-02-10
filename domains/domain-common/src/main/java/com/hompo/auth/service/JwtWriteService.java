package com.hompo.auth.service;

import com.hompo.auth.dto.JwtSecretDto;
import lombok.NonNull;

public interface JwtWriteService {
    String save(@NonNull long userId, @NonNull JwtSecretDto jwtSecretDto);

    void deleteByUserId(long userId);
}
