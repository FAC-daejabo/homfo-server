package com.hompo.auth.filter;

import com.hompo.auth.dto.JwtSecretDto;
import com.hompo.auth.entity.CustomUserDetails;
import com.hompo.auth.infra.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Access Token 기반 JWT Filter 입니다. 매 요청마다 사용합니다.
 */
public class AccessTokenAuthenticationFilter extends OncePerRequestFilter {
    /**
     * AccessToken에 대한 signature 키와 만료시간 정보입니다.
     */
    private final JwtSecretDto jwtSecretDto;

    /**
     * AccessTokenFilter를 적용하지 않는 URI 목록입니다.
     */
    private final List<String> accessTokenWhiteList;

    /**
     * RefreshToken 전용 필터 URI 목록입니다.
     * <p>
     * 이 URI에서는 accessToken이 만료되어도 인증을 허가합니다. accessToken이 없다면 인증을 허가하지 않습니다.
     */
    private final List<String> refreshTokenBlackList;

    public AccessTokenAuthenticationFilter(JwtSecretDto jwtSecretDto, List<String> accessTokenWhiteList, List<String> refreshTokenBlackList) {
        this.jwtSecretDto = jwtSecretDto;
        this.accessTokenWhiteList = accessTokenWhiteList;
        this.refreshTokenBlackList = refreshTokenBlackList;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if (refreshTokenBlackList.contains(requestURI)) {
            validateToken(request, true);
        } else if (!accessTokenWhiteList.contains(requestURI)) {
            validateToken(request, false);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 토큰 인증 여부를 확인합니다.
     * <p>
     * request가 token refresh 용도라면 시그니쳐 키만 확인합니다.
     * 이 외에는 access token이 Expired 인지 확인합니다.
     */
    private void validateToken(HttpServletRequest request, boolean isRefreshToken) {
        String token = getTokenFromRequest(request);

        System.out.println("token123 " + token);

        if (token == null) {
            System.out.println("validate1");
            return;
        }

        if (isRefreshToken) {
            if (JwtUtil.verifyToken(token, jwtSecretDto)) {
                System.out.println("validate2");
                setDetails(request, token);
            }
            return;
        }

        if (JwtUtil.verifyTokenNotExpired(token, jwtSecretDto)) {
            System.out.println("validate3");
            setDetails(request, token);
        }
    }

    /**
     * 인증 정보에 userId 값을 저장합니다.
     */
    private void setDetails(HttpServletRequest request, String token) {
        Long userId = JwtUtil.getUserIdFromToken(token, jwtSecretDto);
        CustomUserDetails userDetails = new CustomUserDetails(userId);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    /**
     * Authorization Header에서 Jwt access token 값을 가져옵니다.
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
