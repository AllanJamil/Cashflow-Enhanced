package com.project.cashflow.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "tokens")
public class Token extends BaseEntity {

    @Column(nullable = false)
    @NotEmpty
    private String token = UUID.randomUUID().toString();

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false)
    @NotNull
    private User user;

    TokenType type;

    private LocalDateTime expiryDate = LocalDateTime.now();


}
