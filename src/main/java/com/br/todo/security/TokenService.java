package com.br.todo.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.br.todo.entity.User;
import com.br.todo.exception.InvalidTokenException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String secret;

    private final String issuer = "todo";

    public String generateToken(User user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                    .withIssuer(issuer)
                    .withClaim("userId", user.getIdUsuario())
                    .withSubject(user.getIdUsuario())
                    .withIssuedAt(generateIssueDate())
                    .withExpiresAt(generateExpirationDate(120L)) //2h
                    .sign(algorithm);

            return token;
        } catch (JWTCreationException e) {
            throw new RuntimeException("Erro ao gerar token de acesso.");
        }
    }

    public String validateToken(String token) {
        if (token != null) {
            token = token.replace("Bearer ", "");
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            String jwtValidated =  JWT.require(algorithm)
                    .withIssuer(issuer)
                    .build()
                    .verify(token)
                    .getSubject();
            return jwtValidated;
        } catch (JWTVerificationException ex) {
            throw new InvalidTokenException("Token de acesso inválido. Por favor, faça login novamente.");
        }
    }

    public String extractUserIdClaim(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);
            return JWT.decode(token).getClaim("userId").asString();
        } else {
            throw new RuntimeException("Erro ao extrair dados do token do usuário");
        }
    }

    private Instant generateExpirationDate(Long minutesToExpire) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset offset = zoneId.getRules().getOffset(LocalDateTime.now());
        return LocalDateTime.now().plusMinutes(minutesToExpire).toInstant(offset);
    }

    private Instant generateIssueDate() {
        ZoneId zoneId = ZoneId.systemDefault();
        ZoneOffset offset = zoneId.getRules().getOffset(LocalDateTime.now());
        return LocalDateTime.now().toInstant(offset);
    }
}
