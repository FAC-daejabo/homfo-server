package com.hompo.config;

import com.hompo.auth.dto.JwtInfoDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = {"classpath:application.yaml"})
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
    public JwtInfoDto accessTokenInfo() {
        return new JwtInfoDto(accessTokenSecret, Long.parseLong(accessTokenExpire));
    }

    @Bean
    public JwtInfoDto refreshTokenInfo() {
        return new JwtInfoDto(refreshTokenSecret, Long.parseLong(refreshTokenExpire));
    }
}
