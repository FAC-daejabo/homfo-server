package com.hompo.auth.entity;

import org.springframework.util.Assert;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

public abstract class Jwt {
    protected final String ENCRYPTED_TOKEN_REGEXP = "^\\$2[ayb]\\$\\d{2}\\$[./A-Za-z0-9]+$";

    public abstract Long getId();

    public abstract String getToken();

    public abstract LocalDateTime getUpdatedAt();

    protected void validateToken(String token) {
        Assert.isTrue(Pattern.matches(ENCRYPTED_TOKEN_REGEXP, Objects.requireNonNull(token)), "올바르지 않은 토큰입니다.");
    }
}
