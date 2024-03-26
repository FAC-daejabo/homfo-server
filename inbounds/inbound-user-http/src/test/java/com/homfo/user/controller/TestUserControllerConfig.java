package com.homfo.user.controller;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
class TestUserControllerConfig {
    @Bean
    @Primary
    public MockUserManagingUsecase manageUserService() {
        return Mockito.mock(MockUserManagingUsecase.class);
    }

    @Bean
    @Primary
    public MockUserValidationUsecase validateUserService() {
        return Mockito.mock(MockUserValidationUsecase.class);
    }
}