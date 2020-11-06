package com.project.cashflow.auth;


import com.project.cashflow.domain.User;
import com.project.cashflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationUserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {


        return new ApplicationUser(User.builder()
                .email("allan@hotmail.com")
                .password(passwordEncoder.encode("Hejlol123"))
                .enabled(false)
                .build());

/*         return userRepository.findByEmail(username).map(ApplicationUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Cant find account with the email: " + username));*/

    }
}
