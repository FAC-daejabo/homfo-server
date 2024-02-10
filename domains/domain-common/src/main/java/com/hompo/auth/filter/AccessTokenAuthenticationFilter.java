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

public class AccessTokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtSecretDto jwtSecretDto;

    private final List<String> accessTokenWhiteList;

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

    private void validateToken(HttpServletRequest request, boolean isRefreshToken) {
        String token = getTokenFromRequest(request);

        if (token == null) {
            return;
        }

        if (isRefreshToken || JwtUtil.verifyToken(token, jwtSecretDto)) {
            setDetails(request, token);
        }
    }

    private void setDetails(HttpServletRequest request, String token) {
        Long userId = JwtUtil.getUserIdFromToken(token, jwtSecretDto);
        CustomUserDetails userDetails = new CustomUserDetails(userId);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
