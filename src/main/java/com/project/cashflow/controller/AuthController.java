package com.project.cashflow.controller;

import com.project.cashflow.domain.User;
import com.project.cashflow.domain.dto.UserDto;
import com.project.cashflow.exception.AccountRecoveryException;
import com.project.cashflow.exception.EntityExistsException;
import com.project.cashflow.exception.ExpiredTokenException;
import com.project.cashflow.exception.InvalidTokenException;
import com.project.cashflow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.security.auth.login.AccountNotFoundException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {

        userDto.setMembers(Collections.emptyList());
        userDto.setPayments(Collections.emptyList());
        try {
            User newUser = authService.createUser(userDto.convertToEntity());
            return ResponseEntity.ok(newUser.convertToDto());
        } catch (EntityExistsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("resend")
    public ResponseEntity<?> sendVerificationMailAgain(@RequestParam String email) {
        try {
            authService.resendVerificationMail(email.trim());
            return ResponseEntity.ok("Email sent.");
        } catch (EntityExistsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (AccountNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("recovery")
    public ResponseEntity<?> passwordResetQuery(@RequestParam String email) {

        try {
            authService.recoverAccount(email.trim());
            return ResponseEntity.ok("Recovery email sent");
        } catch (AccountNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (AccountRecoveryException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    //TODO REDIRECT TO A SERVER PAGE THAT REDIRECTS TO CLIENT WITH TOKEN
    @GetMapping("reset")
    public void resetPasswordRedirection(HttpServletResponse response, @RequestParam String token) throws IOException {
        response.sendRedirect("/passwordrecovery.html?" + token);
    }

    @PatchMapping("password/reset/{token}")
    public ResponseEntity<?> changePassword(@PathVariable String token, @RequestParam String password) {
        try {
            authService.resetPassword(token, password);
            return ResponseEntity.ok().build();
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (ExpiredTokenException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }
}
