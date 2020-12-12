package com.project.cashflow.domain.dto;

import com.project.cashflow.domain.Member;
import com.project.cashflow.domain.Payment;
import com.project.cashflow.domain.User;
import com.project.cashflow.validator.ValidPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    @Email
    private String email;

    @ValidPassword
    private String password;

    @Size(max = 10, message = "Can't save more than 10 members")
    private List<MemberDto> members = new ArrayList<>();

    private List<PaymentDto> payments = new ArrayList<>();

    @Builder
    public UserDto(String email,
                   String password,
                   List<Member> members,
                   List<Payment> payments) {
        this.email = email;
        this.password = password;
        this.members = members.stream()
                .map(Member::convertToDto)
                .collect(Collectors.toList());
        this.payments = payments.stream()
                .map(Payment::convertToDto)
                .collect(Collectors.toList());
    }

    public User convertToEntity() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .members(this.members
                        .stream()
                        .map(MemberDto::convertToEntity)
                        .collect(Collectors.toList())
                )
                .payments(this.payments
                        .stream()
                        .map(PaymentDto::convertToEntity)
                        .collect(Collectors.toList())
                )
                .build();
    }

}
