package com.project.cashflow.auth;


import com.project.cashflow.domain.User;
import com.project.cashflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ApplicationUserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Set<User> users = Set.of(User.builder()
                .email("allan@hotmail.com")
                .password(passwordEncoder.encode("Hejlol123"))
                .enabled(true)
                .build(),
                User.builder()
                        .email("baby@hotmail.com")
                        .password(passwordEncoder.encode("Hejlol123"))
                        .enabled(true)
                        .build()
        );

        User loggedInUser = users.stream().filter(user -> user.getEmail().equalsIgnoreCase(username)).findFirst().get();

        return new ApplicationUser(loggedInUser);

    }
}
