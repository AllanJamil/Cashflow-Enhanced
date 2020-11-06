package com.project.cashflow.service;

import com.project.cashflow.domain.Token;
import com.project.cashflow.domain.User;
import com.project.cashflow.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.project.cashflow.domain.TokenType.*;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;

    public Token createVerificationToken(User user) {
        Token verificationToken = new Token();
        verificationToken.setType(ACTIVATE);
        verificationToken.setUser(user);

        return tokenRepository.save(verificationToken);
    }

    public Token createRecoveryToken(User user) {
        Token recoveryToken = new Token();
        recoveryToken.setType(RECOVERY);
        recoveryToken.setUser(user);

        return tokenRepository.save(recoveryToken);
    }
}

