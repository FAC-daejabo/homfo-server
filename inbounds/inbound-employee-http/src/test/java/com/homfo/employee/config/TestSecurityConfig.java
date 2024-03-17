package com.homfo.employee.config;

import com.homfo.auth.dto.JwtSecretDto;
import com.homfo.auth.filter.AccessTokenAuthenticationFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

@TestConfiguration
@EnableWebSecurity
@EnableConfigurationProperties
public class TestSecurityConfig {
    /**
     * 사용자 액세스 토큰 정보입니다.
     */
    private final JwtSecretDto accessTokenInfo =  new JwtSecretDto("abcdefg", Long.parseLong("60"));

    /**
     * 사용자 액세스 토큰 허용 URI 목록입니다.
     */
    private final List<String> userAccessTokenWhiteList = List.of("/users/register", "/users/sign-in");

    /**
     * 사용자 리프레쉬 토큰 미허용 URI 목록입니다.
     */
    private final List<String> userRefreshTokenBlackList = List.of("/users/refresh");

    /**
     * 직 액세스 토큰 허용 URI 목록입니다.
     */
    private final List<String> employeeAccessTokenWhiteList = List.of("/employees/register", "/employees/sign-in");

    /**
     * 직원 리프레쉬 토큰 미허용 URI 목록입니다.
     */
    private final List<String> employeeRefreshTokenBlackList = List.of("/employees/refresh");

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
        List<String> accessTokenWhiteList = new ArrayList<>(WHITE_LIST);
        List<String> refreshTokenBlackList = new ArrayList<>();

        whiteList.addAll(userAccessTokenWhiteList);
        whiteList.addAll(employeeAccessTokenWhiteList);

        refreshTokenBlackList.addAll(userRefreshTokenBlackList);
        refreshTokenBlackList.addAll(employeeRefreshTokenBlackList);

        accessTokenWhiteList.addAll(whiteList);

        return httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(whiteList.toArray(new String[0])).permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .addFilterBefore(new AccessTokenAuthenticationFilter(accessTokenInfo, accessTokenWhiteList, refreshTokenBlackList), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
