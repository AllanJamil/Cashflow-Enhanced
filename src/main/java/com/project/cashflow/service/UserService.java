package com.project.cashflow.service;

import com.project.cashflow.auth.ApplicationUserService;
import com.project.cashflow.controller.request.PasswordRequestData;
import com.project.cashflow.domain.User;
import com.project.cashflow.exception.EntityNotFoundException;
import com.project.cashflow.exception.PasswordMisMatchException;
import com.project.cashflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountNotFoundException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ApplicationUserService applicationUserService;

    public void changePassword(PasswordRequestData passwordData) throws AccountNotFoundException, PasswordMisMatchException {
        String email = CurrentRequest.getUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AccountNotFoundException("Cannot find user"));

        if (!passwordEncoder.matches(passwordData.getOldPassword(), user.getPassword()))
            throw new PasswordMisMatchException("The password does not match");

        String hashedPassword = passwordEncoder.encode(passwordData.getNewPassword());
        user.setPassword(hashedPassword);
        User updatedUser = userRepository.save(user);
        applicationUserService.loadUserByUsername(updatedUser.getEmail());
    }

    public void deleteUser(String password) throws PasswordMisMatchException, EntityNotFoundException {
        String email = CurrentRequest.getUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Unexpected error, user not present."));

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new PasswordMisMatchException("Felaktigt lösenord.");

        userRepository.delete(user);
    }
}
