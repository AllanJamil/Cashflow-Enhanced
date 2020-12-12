package com.project.cashflow.domain;

import com.project.cashflow.domain.dto.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "members")
public class Member extends BaseEntity {

    @Column(nullable = false, length = 30)
    @Size(min = 3, max = 30, message = "Name needs to have at least 3 and maximum 30 characters")
    private String name;

    /*@LazyCollection(LazyCollectionOption.FALSE)*/
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "member")
    @Size(max = 50, message = "Member cannot have more than 50 bills")
    private List<Bill> bills = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "member")
    private List<PaymentDetails> paymentDetailsList = new ArrayList<>();

    @ManyToOne
    private User user;

    @Builder
    public Member(UUID id, String name, List<Bill> bills, List<PaymentDetails> paymentDetailsList, User user) {
        super(id);
        this.name = name;
        this.bills = bills;
        this.paymentDetailsList = paymentDetailsList;
        this.user = user;
    }

    public MemberDto convertToDto() {
        return MemberDto.builder()
                .id(this.getId())
                .name(this.name)
                .bills(this.bills)
                .build();
    }
}
