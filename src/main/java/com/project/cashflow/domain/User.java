package com.project.cashflow.domain;


import com.project.cashflow.domain.dto.UserDto;
import com.project.cashflow.validator.ValidPassword;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User extends BaseEntity{

    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false)
    @ValidPassword
    private String password;

    private boolean enabled = false;

    private LocalDate lastActive;

    @Size(max = 10, message = "Can't save more than 10 members")
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Member> members;

/*    @LazyCollection(LazyCollectionOption.FALSE)*/
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Payment> payments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    List<Token> tokens;

    @Builder
    public User(UUID id, String email, String password, List<Member> members, LocalDate lastActive, List<Payment> payments, boolean enabled) {
        super(id);
        this.email = email;
        this.password = password;
        this.members = members;
        this.lastActive = lastActive;
        this.payments = payments;
        this.enabled = enabled;
    }

    public UserDto convertToDto() {
        return UserDto.builder()
                .email(this.email)
                .members(this.members)
                .payments(this.payments)
                .build();
    }
}
