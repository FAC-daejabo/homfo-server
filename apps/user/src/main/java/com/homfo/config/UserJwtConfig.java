package com.homfo.config;

import com.homfo.auth.dto.JwtSecretDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

import java.util.List;

// TODO: application-user.yaml 로딩 안되는 버그 수정
@Configuration
@PropertySource(value = {"classpath:application-user.yaml"})
public class UserJwtConfig {
    @Value("${jwt.accessTokenSecret}")
    private String accessTokenSecret;

    @Value("${jwt.accessTokenExpire}")
    private String accessTokenExpire;

    @Value("${jwt.refreshTokenSecret}")
    private String refreshTokenSecret;

    @Value("${jwt.refreshTokenExpire}")
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
