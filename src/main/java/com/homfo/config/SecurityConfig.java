package com.homfo.config;

import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.filter.AccessTokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@RequiredArgsConstructor
public class SecurityConfig {
    /**
     * 사용자 액세스 토큰 정보입니다.
     */
    private final JwtSecretDto userAccessTokenInfo;

    /**
     * 사용자 액세스 토큰 허용 URI 목록입니다.
     */
    private final List<String> userAccessTokenWhiteList;

    /**
     * 사용자 리프레쉬 토큰 미허용 URI 목록입니다.
     */
    private final List<String> userRefreshTokenBlackList;

    /**
     * 직원 액세스 토큰 정보입니다.
     */
    private final JwtSecretDto employeeAccessTokenInfo;

    /**
     * 직원 액세스 토큰 허용 URI 목록입니다.
     */
    private final List<String> employeeAccessTokenWhiteList;

    /**
     * 직원 리프레쉬 토큰 미허용 URI 목록입니다.
     */
    private final List<String> employeeRefreshTokenBlackList;

    /**
     * 서비스 화이트 리스트입니다.
     * <p>
     * TODO: public network에서 접속 불가능하도록 막기, private network에서만 접속 가능하게 변경
     */
    private final List<String> WHITE_LIST = new ArrayList<>(List.of(
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/actuator/**"
    ));

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        List<String> whiteList = new ArrayList<>(WHITE_LIST);
        List<String> userWhiteList = new ArrayList<>(WHITE_LIST);
        List<String> employeeWhiteList = new ArrayList<>(WHITE_LIST);

        whiteList.addAll(userAccessTokenWhiteList);
        whiteList.addAll(employeeAccessTokenWhiteList);

        userWhiteList.add("/employees/**");
        userWhiteList.addAll(userAccessTokenWhiteList);

        employeeWhiteList.add("/users/**");
        employeeWhiteList.addAll(employeeAccessTokenWhiteList);

        return httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(whiteList.toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .addFilterBefore(new AccessTokenAuthenticationFilter(userAccessTokenInfo, userWhiteList, userRefreshTokenBlackList), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new AccessTokenAuthenticationFilter(employeeAccessTokenInfo, employeeWhiteList, employeeRefreshTokenBlackList), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}