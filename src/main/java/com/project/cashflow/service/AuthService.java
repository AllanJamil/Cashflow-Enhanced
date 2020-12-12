package com.project.cashflow.service;

import com.project.cashflow.auth.AccountTokenHandler;
import com.project.cashflow.domain.Member;
import com.project.cashflow.domain.Token;
import com.project.cashflow.domain.TokenType;
import com.project.cashflow.domain.User;
import com.project.cashflow.exception.AccountRecoveryException;
import com.project.cashflow.exception.EntityExistsException;
import com.project.cashflow.exception.ExpiredTokenException;
import com.project.cashflow.exception.InvalidTokenException;
import com.project.cashflow.repository.MemberRepository;
import com.project.cashflow.repository.TokenRepository;
import com.project.cashflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.security.auth.login.AccountNotFoundException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountTokenHandler accountTokenHandler;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;



    public User createUser(@Valid User user) throws EntityExistsException {

        Optional<User> userOptional = userRepository.findByEmail(user.getEmail());

        if (userOptional.isPresent())
            throw new EntityExistsException("Email is already in use");

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        user.setEmail(user.getEmail().toLowerCase());

        //TODO Add verification email
        //TODO Change to default value if possible
        User savedUser = userRepository.save(user);
        accountTokenHandler.confirmRegistration(user);
        Member member = Member.builder()
                .name("HOUSEHOLD")
                .user(savedUser)
                .build();

        memberRepository.save(member);
        return savedUser;
    }

    public void resendVerificationMail(String email) throws AccountNotFoundException, EntityExistsException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("Cannot find account associated with email " + email));

        if (user.isEnabled())
            throw new EntityExistsException("Account is already activated");

        String token = user.getTokens().get(0).getToken();

        emailService.sendEmailConfirmation(email, token);
    }

    public void recoverAccount(String email) throws AccountNotFoundException, AccountRecoveryException {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isEmpty())
            throw new AccountNotFoundException(String.format("No account associated with %s", email));

        User user = userOptional.get();

        if (!user.isEnabled())
            throw new AccountRecoveryException("Account has not yet been activated");

        accountTokenHandler.commitRecovery(user);
    }

    public void resetPassword(String token, String password) throws InvalidTokenException, ExpiredTokenException {
        Optional<Token> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty() || !optionalToken.get().getType().equals(TokenType.RECOVERY))
            throw new InvalidTokenException("Invalid token");

        Token recoveryToken = optionalToken.get();
        User user = userRepository.findById(recoveryToken.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("Unexpected error, no user present."));

        if (recoveryToken.getExpiryDate().isBefore(LocalDateTime.now(ZoneId.of("Europe/Stockholm")))) {
            tokenRepository.delete(recoveryToken);
            throw new ExpiredTokenException("Token has expired.");
        }

        String hashedPassword = passwordEncoder.encode(password);
        user.setPassword(hashedPassword);
        user.setTokens(Collections.emptyList());
        tokenRepository.delete(recoveryToken);
        userRepository.save(user);
    }
}
