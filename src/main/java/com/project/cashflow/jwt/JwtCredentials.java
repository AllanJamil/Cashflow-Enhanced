package com.project.cashflow.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

import javax.crypto.SecretKey;

@AllArgsConstructor
public class JwtCredentials {

    public static final SecretKey secretKey = Keys.hmacShaKeyFor(System.getenv("JWT_KEY").getBytes());
    public static final String prefix = System.getenv("JWT_PREFIX");
    public static final Integer tokenExpirationAfterDays = Integer.parseInt(System.getenv("JWT_EXPIRATION_DAYS"));
    public static final String authorizationHeader = "Authorization";

}
