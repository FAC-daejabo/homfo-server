package com.homfo.user.config;


import com.homfo.auth.dto.JwtSecretDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserJwtConfig {
    @Value("${jwt.user.accessTokenSecret}")
    private String accessTokenSecret;

    @Value("${jwt.user.accessTokenExpire}")
    private String accessTokenExpire;

    @Value("${jwt.user.refreshTokenSecret}")
    private String refreshTokenSecret;

    @Value("${jwt.user.refreshTokenExpire}")
    private String refreshTokenExpire;

    @Bean
    public JwtSecretDto userAccessTokenInfo() {
        return new JwtSecretDto(accessTokenSecret, Long.parseLong(accessTokenExpire));
    }

    @Bean
    public JwtSecretDto userRefreshTokenInfo() {
        return new JwtSecretDto(refreshTokenSecret, Long.parseLong(refreshTokenExpire));
    }

    @Bean
    public List<String> userAccessTokenWhiteList() {
        return List.of("/users/register", "/users/sign-in");
    }

    @Bean
    public List<String> userRefreshTokenBlackList() {
        return List.of("/users/refresh");
    }
}
