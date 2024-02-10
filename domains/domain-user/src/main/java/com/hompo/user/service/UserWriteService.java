package com.hompo.user.service;

import com.hompo.auth.dto.JwtDto;
import com.hompo.user.dto.RegisterCommand;
import lombok.NonNull;
import com.hompo.user.dto.SignInCommand;

import java.util.function.Function;

public interface UserWriteService {
    /**
     * 회원가입합니다.
     * */
    JwtDto register(@NonNull RegisterCommand command, @NonNull Function<Long, String> getAccessToken, @NonNull Function<Long, String> getRefreshToken);

    /**
     * 로그인합니다.
     * */
    JwtDto signIn(@NonNull SignInCommand command, @NonNull JwtDto jwtDto);

    /**
     * 로그아웃합니다.
     * */
    void signOut(long userId);

    /**
     * 계정 정보를 삭제합니다.
     * */
    void deleteAccount(long userId);
}
