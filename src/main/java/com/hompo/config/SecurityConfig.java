package com.hompo.config;

import com.hompo.auth.dto.JwtSecretDto;
import com.hompo.auth.filter.AccessTokenAuthenticationFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableConfigurationProperties
public class SecurityConfig {
    private final JwtSecretDto userAccessTokenInfo;

    private final List<String> userAccessTokenWhiteList;

    private final List<String> userRefreshTokenBlackList;

    private final List<String> WHITE_LIST = new ArrayList<>(List.of(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/actuator/**"
    ));

    public SecurityConfig(
            JwtSecretDto userAccessTokenInfo,
            List<String> userAccessTokenWhiteList,
            List<String> userRefreshTokenBlackList
    ) {
        this.userAccessTokenInfo = userAccessTokenInfo;
        this.userAccessTokenWhiteList = userAccessTokenWhiteList;
        this.userRefreshTokenBlackList = userRefreshTokenBlackList;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        WHITE_LIST.addAll(userAccessTokenWhiteList);

        return httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST.toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .addFilterBefore(new AccessTokenAuthenticationFilter(userAccessTokenInfo, userAccessTokenWhiteList, userRefreshTokenBlackList), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}