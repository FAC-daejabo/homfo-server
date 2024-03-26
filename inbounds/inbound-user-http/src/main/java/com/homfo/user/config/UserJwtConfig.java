package com.homfo.user.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserJwtConfig {
    @Bean
    public List<String> userAccessTokenWhiteList() {
        return List.of(
                "/users/register",
                "/users/sign-in",
                "/users/validate/smsCode",
                "/users/validate/duplicateAccount",
                "/users/validate/duplicateNickname"
        );
    }

    @Bean
    public List<String> userRefreshTokenBlackList() {
        return List.of("/users/refresh");
    }
}
