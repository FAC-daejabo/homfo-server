package com.homfo.employee.config;


import com.homfo.auth.dto.JwtSecretDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class EmployeeJwtConfig {
    @Value("${jwt.employee.accessTokenSecret}")
    private String accessTokenSecret;

    @Value("${jwt.employee.accessTokenExpire}")
    private String accessTokenExpire;

    @Value("${jwt.employee.refreshTokenSecret}")
    private String refreshTokenSecret;

    @Value("${jwt.employee.refreshTokenExpire}")
    private String refreshTokenExpire;

    @Bean
    public JwtSecretDto employeeAccessTokenInfo() {
        return new JwtSecretDto(accessTokenSecret, Long.parseLong(accessTokenExpire));
    }

    @Bean
    public JwtSecretDto employeeRefreshTokenInfo() {
        return new JwtSecretDto(refreshTokenSecret, Long.parseLong(refreshTokenExpire));
    }

    @Bean
    public List<String> employeeAccessTokenWhiteList() {
        return List.of("/employees/register", "/employees/sign-in");
    }

    @Bean
    public List<String> employeeRefreshTokenBlackList() {
        return List.of("/employees/refresh");
    }
}
