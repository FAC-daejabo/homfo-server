package com.hompo.auth.infra.util;

import com.hompo.auth.dto.JwtSecretDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

@Service
public class JwtUtil {
    private static final String key = "id";

    public static String createToken(Long userId, JwtSecretDto jwtSecretDto) {
        return JWT.create()
                .withClaim(key, userId)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtSecretDto.expireTime()))
                .sign(Algorithm.HMAC512(jwtSecretDto.secretKey()));
    }

    public static boolean verifyToken(String token, JwtSecretDto jwtSecretDto) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(jwtSecretDto.secretKey())).build();
        return verifier.verify(token).getExpiresAt().compareTo(Date.from(Instant.now())) > 0;
    }

    public static Long getUserIdFromToken(String token, JwtSecretDto jwtSecretDto) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(jwtSecretDto.secretKey())).build();
        return verifier.verify(token).getClaim(key).asLong();
    }
}
