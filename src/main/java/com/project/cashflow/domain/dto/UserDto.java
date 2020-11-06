package com.project.cashflow.domain.dto;

import com.project.cashflow.domain.Member;
import com.project.cashflow.domain.Payment;
import com.project.cashflow.domain.User;
import com.project.cashflow.validator.ValidPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class UserDto {

    @Email
    private String email;

    @ValidPassword
    private String password;

    @Size(max = 10, message = "Can't save more than 10 members")
    private List<Member> members;

    private List<Payment> payments;



    @Builder
    public UserDto(String email,
                   String password,
                   List<Member> members,
                   List<Payment> payments) {
        this.email = email;
        this.password = password;
        this.members = members;
        this.payments = payments;
    }

    public User convertToEntity() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .members(this.members)
                .payments(this.payments)
                .build();
    }

}
