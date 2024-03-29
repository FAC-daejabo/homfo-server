package com.homfo.auth.infra.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.homfo.auth.dto.JwtSecretDto;

import java.time.Instant;
import java.util.Date;

/**
 * JWT 관련 유틸 함수입니다.
 */
public class JwtUtil {
    private static final String KEY = "id";

    /**
     * 토큰을 생성합니다.
     * <p>
     * payload에는 {id: id} 정보만 저장합니다.
     */
    public static String createToken(Long id, JwtSecretDto jwtSecretDto) {
        return JWT.create()
                .withClaim(KEY, id)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtSecretDto.expireTime()))
                .sign(Algorithm.HMAC512(jwtSecretDto.secretKey()));
    }

    /**
     * 토큰의 signature가 맞는지만 확인합니다.
     */
    public static boolean verifyToken(String token, JwtSecretDto jwtSecretDto) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(jwtSecretDto.secretKey())).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    /**
     * 토큰의 signature를 확인하고, 아직 만료되지 않았을 때만 true압니다.
     */
    public static boolean verifyTokenNotExpired(String token, JwtSecretDto jwtSecretDto) {
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC512(jwtSecretDto.secretKey())).build();
            return verifier.verify(token).getExpiresAt().compareTo(Date.from(Instant.now())) > 0;
        } catch (JWTVerificationException exception) {
            return false;
        }
    }

    /**
     * 토큰에서 id 값을 가져옵니다.
     */
    public static Long getIdFromToken(String token, JwtSecretDto jwtSecretDto) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(jwtSecretDto.secretKey())).build();
        return verifier.verify(token).getClaim(KEY).asLong();
    }
}
