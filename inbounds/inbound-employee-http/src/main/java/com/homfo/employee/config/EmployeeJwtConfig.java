package com.homfo.employee.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class EmployeeJwtConfig {
    @Bean
    public List<String> employeeAccessTokenWhiteList() {
        return List.of("/employees/register", "/employees/sign-in");
    }

    @Bean
    public List<String> employeeRefreshTokenBlackList() {
        return List.of("/employees/refresh");
    }
}
