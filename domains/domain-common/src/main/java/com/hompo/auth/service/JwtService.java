package com.hompo.auth.service;

import com.hompo.auth.dto.JwtInfoDto;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    public String createToken(String subject, JwtInfoDto jwtInfoDto) {
        return JWT.create()
                .withClaim("id", subject)
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtInfoDto.expireTime()))
                .sign(Algorithm.HMAC512(jwtInfoDto.secretKey()));
    }

    public DecodedJWT verifyToken(String token, JwtInfoDto jwtInfoDto) throws JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC512(jwtInfoDto.secretKey())).build();
        return verifier.verify(token);
    }
}
