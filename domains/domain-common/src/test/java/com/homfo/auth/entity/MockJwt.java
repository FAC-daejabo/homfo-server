package com.homfo.auth.entity;

import java.time.LocalDateTime;

public class MockJwt extends  Jwt {
    private Long id;

    private String token;

    public MockJwt(Long id, String token) {
        this.id = id;
        this.token = token;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return null;
    }
}
