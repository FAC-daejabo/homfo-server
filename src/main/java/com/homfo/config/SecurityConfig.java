package com.homfo.config;

import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.filter.AccessTokenAuthenticationFilter;
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
    /**
     * 사용자 액세스 토큰 정보입니다.
     * */
    private final JwtSecretDto userAccessTokenInfo;

    /**
     * 사용자 액세스 토큰 허용 URI 목록입니다.
     * */
    private final List<String> userAccessTokenWhiteList;

    /**
     * 사용자 리프레쉬 토큰 미허용 URI 목록입니다.
     * */
    private final List<String> userRefreshTokenBlackList;

    /**
     * 서비스 화이트 리스트입니다.
     *
     * TODO: public network에서 접속 불가능하도록 막기, private network에서만 접속 가능하게 변경
     * */
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

        // 화이트 리스트는 허용합니다.
        // 이외에는 JWT 액세스 토큰 인증을 거칩니다.
        return httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(WHITE_LIST.toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .addFilterBefore(new AccessTokenAuthenticationFilter(userAccessTokenInfo, WHITE_LIST, userRefreshTokenBlackList), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}