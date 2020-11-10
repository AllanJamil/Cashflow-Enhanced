package com.project.cashflow.controller;

import com.project.cashflow.domain.User;
import com.project.cashflow.domain.dto.UserDto;
import com.project.cashflow.exception.EntityExistsException;
import com.project.cashflow.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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
}
