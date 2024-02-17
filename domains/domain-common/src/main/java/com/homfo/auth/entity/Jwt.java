package com.homfo.auth.entity;

import lombok.Builder;
import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * JWT entity 입니다. Refresh Token을 대상으로 합니다.
 *
 * Refresh Token은 저장시 암호화해서 저장합니다.
 * */
public abstract class Jwt {
    protected final String ENCRYPTED_TOKEN_REGEXP = "^\\$2[ayb]\\$\\d{2}\\$[./A-Za-z0-9]+$";

    /**
     * Refresh Token 소유자의 ID 값입니다.
     * */
    public abstract Long getId();

    /**
     * 암호화된 Refresh Token 값입니다.
     * */
    public abstract String getToken();

    /**
     * Refresh Token이 수정된 시각입니다.
     * */
    public abstract LocalDateTime getUpdatedAt();

    /**
     * Refresh Token이 암호화되어 있는지 확인합니다.
     * */
    protected void validateToken(String token) {
        Assert.isTrue(Pattern.matches(ENCRYPTED_TOKEN_REGEXP, Objects.requireNonNull(token)), "올바르지 않은 토큰입니다.");
    }
}
