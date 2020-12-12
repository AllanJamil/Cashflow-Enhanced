package com.project.cashflow.auth;


import com.project.cashflow.domain.Token;
import com.project.cashflow.domain.User;
import com.project.cashflow.service.EmailService;
import com.project.cashflow.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountTokenHandler {

  private final TokenService tokenService;
  private final EmailService emailService;

  public void confirmRegistration(User user) {
    Token verificationToken = tokenService.createVerificationToken(user);
    emailService.sendEmailConfirmation(user.getEmail(), verificationToken.getToken());
  }

  public void commitRecovery(User user) {
    Token recoveryToken = tokenService.createRecoveryToken(user);
    emailService.sendRecoveryEmail(user.getEmail(), recoveryToken.getToken());
  }
}
