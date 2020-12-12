package com.project.cashflow.controller.request;

import com.project.cashflow.validator.ValidPassword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class PasswordRequestData {

    @ValidPassword
    private String oldPassword;
    @ValidPassword
    private String newPassword;

}
