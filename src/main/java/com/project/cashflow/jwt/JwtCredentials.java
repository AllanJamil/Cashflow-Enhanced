package com.project.cashflow.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@AllArgsConstructor
@Getter
@Component
public class JwtCredentials {

    private final SecretKey secretKey = Keys.hmacShaKeyFor(System.getenv("JWT_KEY").getBytes());
    private final String prefix = System.getenv("JWT_PREFIX");
    private final Integer tokenExpirationAfterDays = Integer.parseInt(System.getenv("JWT_EXPIRATION_DAYS"));
    private final String authorizationHeader = "Authorization";

}
