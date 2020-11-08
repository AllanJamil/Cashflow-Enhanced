package com.project.cashflow.service;

import com.project.cashflow.domain.Member;
import com.project.cashflow.domain.User;
import com.project.cashflow.exception.EntityExistsException;
import com.project.cashflow.repository.MemberRepository;
import com.project.cashflow.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;


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
        Member member = Member.builder()
                .name("HOUSEHOLD")
                .user(savedUser)
                .build();

        memberRepository.save(member);
        return savedUser;
    }
}
