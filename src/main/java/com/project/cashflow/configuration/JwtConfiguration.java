package com.project.cashflow.configuration;

import com.project.cashflow.jwt.JwtCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
@RequiredArgsConstructor
public class JwtConfiguration {

    private final JwtCredentials jwtCredentials;

    @Bean
    public SecretKey getSecretKey() {
        return jwtCredentials.getSecretKey();
    }

    @Bean
    public String getPrefix() {
        return jwtCredentials.getPrefix();
    }

    @Bean
    public String getAuthorizationHeader() {
        return jwtCredentials.getAuthorizationHeader();
    }

    @Bean
    public Integer getTokenExpirationAfterDays() {
        return jwtCredentials.getTokenExpirationAfterDays();
    }
}
