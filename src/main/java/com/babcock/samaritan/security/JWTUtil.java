package com.babcock.samaritan.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.babcock.samaritan.entity.Token;
import com.babcock.samaritan.repository.TokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {
    @Value("${jwt_secret}")
    private String secret;
    @Autowired
    private TokenRepo tokenRepo;

    public String generateToken(String id) throws IllegalArgumentException, JWTCreationException {
        long now = System.currentTimeMillis();
        return JWT.create()
                .withSubject("User Details")
                .withClaim("user_id", id)
                .withIssuedAt(new Date(now))
                .withIssuer("Babcock Samaritan")
                .sign(Algorithm.HMAC256(secret));
    }

    public String validateTokenAndRetrieveSubject(String token) throws JWTVerificationException {
        if (tokenRepo.existsById(token))
            throw new JWTVerificationException("Token expired");
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("Babcock Samaritan")
                .build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT.getClaim("user_id").asString();
    }

    public boolean invalidateToken(Token token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .withIssuer("Babcock Samaritan")
                .build();
        if (token.getToken().startsWith("Bearer ")) {
            token.setToken(token.getToken().substring(7));
            DecodedJWT decodedJWT = verifier.verify(token.getToken());
            if (decodedJWT != null) {
                tokenRepo.save(token);
                return true;
            }
        }
        return false;
    }
}
