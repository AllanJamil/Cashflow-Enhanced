package com.project.cashflow.service;

import org.springframework.security.core.context.SecurityContextHolder;

public class CurrentRequest {

    public static String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
